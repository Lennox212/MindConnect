package com.example.MindConnect.Payload.Response.BlockedResponse;

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

public class UnblockUserResponse {

    private UUID unblockedUserID;

    private String unblockedUserName;

    private String unblockedBy;

    private LocalDateTime unblockedAt;

    private String message;
}
