package com.example.MindConnect.Payload.Response.UserResponses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UpdateProfilePictureResponse {
    private String message;

    private LocalDateTime localDateTime;
}
