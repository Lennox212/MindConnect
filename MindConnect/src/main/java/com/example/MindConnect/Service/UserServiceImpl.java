package com.example.MindConnect.Service;

import com.example.MindConnect.Config.JwtGenerator;
import com.example.MindConnect.Entity.AccountDeactivationEntity;
import com.example.MindConnect.Entity.UserEntity;
import com.example.MindConnect.Enums.AccountStatus;
import com.example.MindConnect.Enums.Role;
import com.example.MindConnect.Payload.Request.UserRequests.*;
import com.example.MindConnect.Payload.Response.UserResponses.JwtResponse;
import com.example.MindConnect.Payload.Response.UserResponses.UserCreationResponse;
import com.example.MindConnect.Payload.Response.UserResponses.OtpResponse;
import com.example.MindConnect.Repository.AccountDeactivationRepository;
import com.example.MindConnect.Repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContextException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Data
@RequiredArgsConstructor



public class UserServiceImpl {

    private final UserRepository userRepository; //your own bean(object)

    private final PasswordEncoder passwordEncoder; //spring bean (already exists)

    private final JwtGenerator jwtGenerator;

    private final AuthenticationManager authenticationManager;

    private final OtpService otpService;

    private final EmailService emailService;

    private final AccountDeactivationRepository deactivationRepository;

    public UserCreationResponse createUser(UserCreationRequest request) {

        //TODO--CUSTOM EXCEPTIONS


        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("User with the provided details already exists");
        }

        UserEntity user = UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .gender(request.getGender())
                .mentalCondition(request.getMentalCondition())
                .signUpDate(LocalDateTime.now())
                .role(Role.USER)
                .status(AccountStatus.INACTIVE)
                .build();

        UserEntity new_user = userRepository.save(user);

        UserCreationResponse response = UserCreationResponse.builder()
                .firstName(new_user.getFirstName())
                .lastName(new_user.getLastName())
                .email(new_user.getEmail())
                .id(new_user.getId())
                .gender(new_user.getGender())
                .signUpDate(LocalDateTime.now())
                .accountStatus(AccountStatus.INACTIVE)
                .build();
        return response;
    }

    public UserCreationResponse getUserByID(UUID id) {
//TODO--CUSTOM EXCEPTIONS
        UserEntity found_user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("404! User with ID entered not found!"));

        UserCreationResponse response = UserCreationResponse.builder()
                .firstName(found_user.getFirstName())
                .lastName(found_user.getLastName())
                .gender(found_user.getGender())
                .signUpDate(LocalDateTime.now())
                .build();

        return response;
    }


    public List<UserEntity> getAllUsers() {

        return userRepository.findAll();
    }


    public JwtResponse userLogin(LoginRequest loginRequest) {



        UserEntity user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()->new RuntimeException("Invalid email or password."));





            if (user.getStatus() == AccountStatus.INACTIVE) {
                throw new ApplicationContextException("Account Inactive. Please check your email to active your account");
            }

            if (user.getStatus() == AccountStatus.FROZEN) {

                throw new ApplicationContextException("Account Frozen. Please contact support!");
            }

            if (user.getStatus() == AccountStatus.SUSPENDED) {
                throw new ApplicationContextException("Account suspended. Please check your email for further instructions");
            }

            if(user.getStatus() == AccountStatus.DEACTIVATED) {
                throw new ApplicationContextException("User disabled account.Please re-activate the account to regain access");
            }




        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())); //makes sure the info matches the info in database. Authenticates, tehn creates an object if truwe

        SecurityContextHolder.getContext().setAuthentication(authentication); //Store the authenticated user as the current user

        String token = jwtGenerator.generateToken(authentication); //generate token


        JwtResponse response = JwtResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .token(token)
                .localDateTime(LocalDateTime.now())
                .message("Login successful!")
                .build();

        return response;
    }


    public OtpResponse sendPasswordResetOtp(SendOtpRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                new RuntimeException("Unable to locate the authenticated user account."));


        String otp = otpService.generateOtp(request.getEmail());
        emailService.sendOtpEmail(user, otp);


        OtpResponse response = OtpResponse.builder()
                .message("OTP sent for password reset verification")
                .email(user.getEmail())
                .timestamp(LocalDateTime.now())
                .build();
        return response;


    }

    public OtpResponse verifyOtpAndResetPassword(VerifyOtpRequest request) {

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Unable to locate the authenticated user account."));


        if (otpService.validateOtp(request.getEmail(), request.getOtp())) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            otpService.clearOtp(request.getEmail()); //clear - once used no need to reuse/store it again
        } else {
            throw new RuntimeException("The OTP entered is incorrect");
        }

        OtpResponse response = OtpResponse.builder()
                .email(user.getEmail())
                .message("Password reset successful")
                .timestamp(LocalDateTime.now())
                .build();

        return response;
    }


    public OtpResponse sendAccountActivationOtp(SendOtpRequest request) {

        UserEntity user = userRepository.findByEmail(request.getEmail()).
                orElseThrow(() -> new RuntimeException("Unable to locate the authenticated user account."));

        if (user.getStatus() == AccountStatus.ACTIVE) {
            throw new RuntimeException("Account has already been activated");
        }

        String otp = otpService.generateOtp(user.getEmail());

        emailService.sendOtpEmail(user, otp);

        OtpResponse response = OtpResponse.builder()
                .email(user.getEmail())
                .message("Please check your email for the account activation code")
                .timestamp(LocalDateTime.now())
                .build();
        return response;
    }

    public OtpResponse activateAccount(ActivateAccountRequest request) {

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Unable to locate the authenticated user account."));

        if (otpService.validateOtp(request.getEmail(), request.getOtp())) {
            user.setStatus(AccountStatus.ACTIVE);
            userRepository.save(user);
            otpService.clearOtp(request.getEmail());
        } else {
            throw new RuntimeException("Account could not be activated. Please try again");
        }

        OtpResponse response = OtpResponse.builder()
                .email(user.getEmail())
                .message("Account has been activated successfully")
                .timestamp(LocalDateTime.now())
                .build();

        return response;
    }
    public OtpResponse changePassword(ChangePasswordRequest request){

        String email = SecurityContextHolder.getContext().getAuthentication().getName(); //get the username of
        //currently logged user

        UserEntity user = userRepository.findByEmail(email).
                orElseThrow(()->new RuntimeException("Unable to locate the authenticated user account."));

        Boolean match = passwordEncoder.matches(request.getOldPassword(), user.getPassword());


        if(!match){
            throw new RuntimeException("Passwords do not match. Please try again");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        OtpResponse response = OtpResponse.builder()
                .message("Password has been changed successfully")
                .email(user.getEmail())
                .timestamp(LocalDateTime.now())
                .build();
        return response;
    }

    public OtpResponse updateProfile(UpdateProfileRequest request){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("Unable to locate the authenticated user account."));


        if(request.getFirstName() != null) {

            if(request.getFirstName().isBlank()){
                throw new RuntimeException("First Name cannot be blank");
            }

            user.setFirstName(request.getFirstName());
        }



        if(request.getLastName() != null) {

            if(request.getLastName().isBlank()){
                throw new RuntimeException("Last Name cannot be blank");
            }

            user.setLastName(request.getLastName());
        }



        if(request.getGender() != null) {
            user.setGender(request.getGender());
        }

        if(request.getMentalCondition() != null) {
            user.setMentalCondition(request.getMentalCondition());
        }

        userRepository.save(user);

        OtpResponse response = OtpResponse.builder()
                .email(user.getEmail())
                .message("Profile updated successfully")
                .timestamp(LocalDateTime.now())
                .build();

        return response;


    }


    public OtpResponse deactivateAccount(DeactivateAccountRequest request){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Unable to locate the authenticated user account."));

        if(user.getStatus() == AccountStatus.DEACTIVATED){
            throw new RuntimeException("Account has already been deactivated");
        }

        boolean match = passwordEncoder.matches(request.getPasswordConfirmation(), user.getPassword());

        if(!match){
            throw new RuntimeException("The password entered does not match your current password.");
        }

        //TODO soft deactivation - limit users access to app so they can activate themselves
        //TODO check responses - null
        //TODO write controller ASAP
        //TODO - Custom Exceptions


            AccountDeactivationEntity deactivation = AccountDeactivationEntity.builder()
                    .user(user)
                    .reason(request.getReason())
                    .deactivatedAt(LocalDateTime.now())
                    .status(AccountStatus.DEACTIVATED)
                    .build();
            deactivationRepository.save(deactivation);

            user.setStatus(AccountStatus.DEACTIVATED);
            userRepository.save(user);

        OtpResponse response = OtpResponse.builder()
                .email(user.getEmail())
                .message("Account has been deactivated successfully")
                .timestamp(LocalDateTime.now())
                .build();

        return response;
    }



}
