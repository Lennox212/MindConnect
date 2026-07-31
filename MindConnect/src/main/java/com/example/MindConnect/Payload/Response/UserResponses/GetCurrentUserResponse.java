package com.example.MindConnect.Payload.Response.UserResponses;

import com.example.MindConnect.Enums.AccountStatus;
import com.example.MindConnect.Enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetCurrentUserResponse {

    private UUID userID;

    private String firstName;

    private String lastName;

    private String email;

    private LocalDateTime signUpDate;

    private AccountStatus status;

    private String profilePictureUrl;





}
