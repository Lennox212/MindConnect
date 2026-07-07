package com.example.MindConnect.Payload.Request.UserRequests;


import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class LoginRequest {
    @Email
    @Column(unique = true)
    private String email;

    private String password;

}
