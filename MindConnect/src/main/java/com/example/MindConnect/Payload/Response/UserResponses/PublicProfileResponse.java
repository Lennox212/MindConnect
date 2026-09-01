package com.example.MindConnect.Payload.Response.UserResponses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class PublicProfileResponse {
    private UUID userID;

    private String firstName;

    private String lastName;

    private String bio;

    private String profilePictureUrl;

    private LocalDateTime signUpDate;
}
