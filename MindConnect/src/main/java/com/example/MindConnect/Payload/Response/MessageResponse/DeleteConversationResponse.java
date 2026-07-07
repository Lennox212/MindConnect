package com.example.MindConnect.Payload.Response.MessageResponse;

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

public class DeleteConversationResponse {

    private UUID otherUserID;

    private String otherUserName;

    private long deletedMessageCount;

    private LocalDateTime deletedAt;

    private String status;




}
