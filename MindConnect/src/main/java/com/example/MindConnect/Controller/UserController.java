package com.example.MindConnect.Controller;

import com.example.MindConnect.Entity.UserEntity;
import com.example.MindConnect.Payload.Request.UserRequests.*;
import com.example.MindConnect.Payload.Response.UserResponses.JwtResponse;
import com.example.MindConnect.Payload.Response.UserResponses.OtpResponse;
import com.example.MindConnect.Payload.Response.UserResponses.UserCreationResponse;
import com.example.MindConnect.Service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserServiceImpl userService;


@PostMapping("/register")
public UserCreationResponse createUser(@RequestBody UserCreationRequest request){
    System.out.println("REGISTER ENDPOINT HIT");
    return userService.createUser(request);

}

@GetMapping
public List<UserEntity> getAllUsers(){
    return userService.getAllUsers();
}

@GetMapping("/{id}")
public UserCreationResponse getUsersById(@PathVariable UUID id){
    return userService.getUserByID(id);
}

@PostMapping("/login")
public JwtResponse user_login(@RequestBody LoginRequest request){
    return userService.userLogin(request);
}

@PostMapping("/account-activation-otp")
public OtpResponse sendAccountActOtp(@RequestBody SendOtpRequest request){
    System.out.println("ACCOUNT OTP SENT ENDPOINT HIT");
    return userService.sendAccountActivationOtp(request);

}

@PostMapping("/activate-account")
    public OtpResponse activateAcc(@RequestBody ActivateAccountRequest request){
    return userService.activateAccount(request);

}

@PostMapping("/password-reset-otp")
public OtpResponse passwordResetOtp(@RequestBody SendOtpRequest request){
    return userService.sendPasswordResetOtp(request);
}

@PostMapping("/reset-password")
public OtpResponse resetAndVerifyPassword(@RequestBody VerifyOtpRequest request){
    return userService.verifyOtpAndResetPassword(request);
}

@PostMapping("/deactivate-account")
public OtpResponse deactivateAcc(@RequestBody DeactivateAccountRequest request){
    return userService.deactivateAccount(request);
}


@PostMapping("/change-password")
public OtpResponse changePassword(@RequestBody ChangePasswordRequest request){

    return userService.changePassword(request);
}

@PostMapping("/update-profile")
public OtpResponse updateProfile(@RequestBody UpdateProfileRequest request){
    return userService.updateProfile(request);
}



}
