package com.example.MindConnect.Payload.Request.UserRequests;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ActivateAccountRequest {

    private String email;

    private String otp;

}
