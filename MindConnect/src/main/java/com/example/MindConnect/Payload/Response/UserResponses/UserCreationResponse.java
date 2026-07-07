package com.example.MindConnect.Payload.Response.UserResponses;

import com.example.MindConnect.Enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class UserCreationResponse{

    private String firstName;

    private String lastName;

    private String email;

    private UUID id;

    private String gender;

    private LocalDateTime signUpDate;

    private AccountStatus accountStatus;
}
