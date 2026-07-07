package com.example.MindConnect.Payload.Response.BlockedResponse;

import jdk.jfr.Name;
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

public class GetBlockedUsersResponse {

    private UUID blockID;

    private UUID blockedUserID;

    private String blockedUserName;

    private LocalDateTime blockedAt;

    private String motive;




}
