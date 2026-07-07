package com.example.MindConnect.Payload.Response.BlockedResponse;


import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BlockUserResponse {

    private UUID blockID;

    private UUID blockedUserID;

    private String blockedUserName;

    private String blockedBy;

    private LocalDateTime blockedAt;

    private String motive;

    private String message;







}
