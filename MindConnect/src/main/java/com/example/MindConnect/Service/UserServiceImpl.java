package com.example.MindConnect.Service;

import com.example.MindConnect.Config.JwtGenerator;
import com.example.MindConnect.CustomExceptions.*;
import com.example.MindConnect.Entity.AccountDeactivationEntity;
import com.example.MindConnect.Entity.RefreshTokenEntity;
import com.example.MindConnect.Entity.UserEntity;
import com.example.MindConnect.Enums.AccountStatus;
import com.example.MindConnect.Enums.Role;
import com.example.MindConnect.Payload.Request.RefreshTokenRequest.RefreshTokenRequest;
import com.example.MindConnect.Payload.Request.UserRequests.*;
import com.example.MindConnect.Payload.Response.UserResponses.*;
import com.example.MindConnect.Repository.AccountDeactivationRepository;
import com.example.MindConnect.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor



public class UserServiceImpl {

    private final UserRepository userRepository; //your own bean(object)

    private final PasswordEncoder passwordEncoder; //spring bean (already exists)

    private final JwtGenerator jwtGenerator;

    private final AuthenticationManager authenticationManager;

    private final OtpService otpService;

    private final EmailService emailService;

    private final AccountDeactivationRepository deactivationRepository;

    private final RefreshTokenServiceImpl refreshTokenService;

    private final S3Service s3Service;

    public UserCreationResponse createUser(UserCreationRequest request) {


        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User with the provided details already exists");
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

        UserEntity found_user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with ID entered not found!"));

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
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials. Please try again"));


        if (user.getStatus() == AccountStatus.INACTIVE) {
            throw new AccountInactiveException("Account Inactive. Please check your email to active your account");
        }

        if (user.getStatus() == AccountStatus.FROZEN) {

            throw new AccountFrozenException("Account Frozen. Please contact support!");
        }

        if (user.getStatus() == AccountStatus.SUSPENDED) {
            throw new AccountSuspendedException("Account suspended. Please check your email for further instructions");
        }

        if (user.getStatus() == AccountStatus.DEACTIVATED) {
            throw new AccountDeactivatedException("User disabled account.Please re-activate the account to regain access");
        }


        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())); //makes sure the info matches the info in database. Authenticates, tehn creates an object if truwe

        SecurityContextHolder.getContext().setAuthentication(authentication); //Store the authenticated user as the current user

        String token = jwtGenerator.generateAccessToken(authentication); //generate token

        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(user);

        JwtResponse response = JwtResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .accessToken(token)
                .refreshToken(refreshToken.getToken())
                .localDateTime(LocalDateTime.now())
                .message("Login successful!")
                .build();

        return response;
    }


    public UpdateProfilePictureResponse updateProfilePicture(MultipartFile picture) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));

        String oldObjectKey = user.getProfilePictureKey();

        String newObjectKey = s3Service.uploadFile(picture);


        try {
            user.setProfilePictureKey(newObjectKey);
            userRepository.save(user);
        } catch (Exception exception) {

            s3Service.deleteFile(newObjectKey);

            throw exception;
        }

        String profilePictureUrl = s3Service.generatePresignedUrl(newObjectKey);

        if (oldObjectKey != null && !oldObjectKey.isBlank()) {
            try {
                s3Service.deleteFile(oldObjectKey);
            } catch (PictureUploadException exception) {
                log.error("Failed to delete old profile picture {}", oldObjectKey, exception);
            }
        }


        UpdateProfilePictureResponse response = UpdateProfilePictureResponse.builder()
                .message("Profile picture updated successfully")
                .profilePictureUrl(profilePictureUrl)
                .localDateTime(LocalDateTime.now())
                .build();

        return response;


    }

    public JwtResponse refreshAccessToken(RefreshTokenRequest request) {

        String refreshToken = request.getRefreshToken();

        RefreshTokenEntity refreshTokenEntity = refreshTokenService.validateRefreshToken(refreshToken);

        UserEntity user = refreshTokenEntity.getUser();


        String accessToken = jwtGenerator.generateAccessToken(user);

        JwtResponse response = JwtResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .message("Access token generated successfully")
                .localDateTime(LocalDateTime.now())
                .build();
        return response;

    }


    public OtpResponse sendPasswordResetOtp(SendOtpRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                new UserNotFoundException("Unable to locate the authenticated user account."));


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
                .orElseThrow(() -> new UserNotFoundException("Unable to locate the authenticated user account."));


        if (otpService.validateOtp(request.getEmail(), request.getOtp())) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            otpService.clearOtp(request.getEmail()); //clear - once used no need to reuse/store it again
        } else {
            throw new InvalidOTPException("The OTP entered is incorrect");
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
                orElseThrow(() -> new UserNotFoundException("Unable to locate the authenticated user account."));

        if (user.getStatus() == AccountStatus.ACTIVE) {
            throw new AccountAlreadyActiveException("Account has already been activated");
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
                .orElseThrow(() -> new UserNotFoundException("Unable to locate the authenticated user account."));

        if (otpService.validateOtp(request.getEmail(), request.getOtp())) {
            user.setStatus(AccountStatus.ACTIVE);
            userRepository.save(user);
            otpService.clearOtp(request.getEmail());
        } else {
            throw new InvalidOTPException("Account could not be activated. Please try again");
        }

        OtpResponse response = OtpResponse.builder()
                .email(user.getEmail())
                .message("Account has been activated successfully")
                .timestamp(LocalDateTime.now())
                .build();

        return response;
    }

    public OtpResponse changePassword(ChangePasswordRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName(); //get the username of
        //currently logged user

        UserEntity user = userRepository.findByEmail(email).
                orElseThrow(() -> new UserNotFoundException("Unable to locate the authenticated user account."));

        Boolean match = passwordEncoder.matches(request.getOldPassword(), user.getPassword());


        if (!match) {
            throw new PasswordDoesNotMatchException("Passwords do not match. Please try again");
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

    public GetCurrentUserResponse updateProfile(UpdateProfileRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Unable to locate the authenticated user account."));


        if (request.getFirstName() != null) {

            if (request.getFirstName().isBlank()) {
                throw new BlankFieldException("First Name cannot be blank");
            }

            user.setFirstName(request.getFirstName());
        }


        if (request.getLastName() != null) {

            if (request.getLastName().isBlank()) {
                throw new BlankFieldException("Last Name cannot be blank");
            }

            user.setLastName(request.getLastName());
        }

        if (request.getBio() != null) {
            user.setBio(request.getBio().trim());
        }


        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }

        if (request.getMentalCondition() != null) {
            user.setMentalCondition(request.getMentalCondition());
        }

        UserEntity savedUser = userRepository.save(user);

        String pictureUrl = null;

        if (savedUser.getProfilePictureKey() != null &&
                !savedUser.getProfilePictureKey().isBlank()) {

            pictureUrl = s3Service.generatePresignedUrl(savedUser.getProfilePictureKey());


        }
        GetCurrentUserResponse response = GetCurrentUserResponse.builder()
                .userID(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .gender(savedUser.getGender())
                .mentalCondition(savedUser.getMentalCondition())
                .profilePictureUrl(pictureUrl)
                .bio(savedUser.getBio())
                .signUpDate(savedUser.getSignUpDate())
                .status(savedUser.getStatus())
                .build();
        return response;
    }


    public OtpResponse deactivateAccount(DeactivateAccountRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Unable to locate the authenticated user account."));

        if (user.getStatus() == AccountStatus.DEACTIVATED) {
            throw new AccountDeactivatedException("Account has already been deactivated");
        }

        boolean match = passwordEncoder.matches(request.getPasswordConfirmation(), user.getPassword());

        if (!match) {
            throw new PasswordDoesNotMatchException("The password entered does not match your current password.");
        }

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

    public GetCurrentUserResponse getCurrentUser() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String pictureUrl = null;

        if (user.getProfilePictureKey() != null &&
                !user.getProfilePictureKey().isBlank()) {

            pictureUrl = s3Service.generatePresignedUrl(user.getProfilePictureKey());
        }


        GetCurrentUserResponse response = GetCurrentUserResponse.builder()
                .userID(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .profilePictureUrl(pictureUrl)
                .bio(user.getBio())
                .signUpDate(user.getSignUpDate())
                .status(user.getStatus())
                .build();

        return response;

    }

    public OtpResponse logOut(RefreshTokenRequest request) {

        String token = request.getRefreshToken();

        refreshTokenService.deleteRefreshToken(token);

        SecurityContextHolder.clearContext();

        OtpResponse response = OtpResponse.builder()
                .message("Log out successful")
                .timestamp(LocalDateTime.now())
                .build();

        return response;
    }

    public PublicProfileResponse getPublicProfile(UUID id) {

        UserEntity user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User Profile not found"));

        String pictureUrl = null;

        if (user.getProfilePictureKey() != null
                && !user.getProfilePictureKey().isBlank()) {

            pictureUrl = s3Service.generatePresignedUrl(user.getProfilePictureKey());
        }

        PublicProfileResponse response = PublicProfileResponse.builder()
                .userID(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .profilePictureUrl(pictureUrl)
                .bio(user.getBio())
                .signUpDate(user.getSignUpDate())
                .build();
        return response;
    }
}