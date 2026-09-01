package com.example.MindConnect.Payload.Response.MessageResponse;

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


public class SendMessageResponse {


    private UUID messageID;

    private UUID senderID;

    private UUID recipientID;

    private String senderName;

    private String recipientName;

    private String content;

    private LocalDateTime sentAt;

    private boolean read;

    private LocalDateTime readAt;

    private String messageStatus;





}
