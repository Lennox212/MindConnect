package com.example.MindConnect.Payload.Response.MessageResponse;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class GetMyChatsResponse {

    private UUID otherUserID;

    private String otherUserName;

    private String lastMessage;

    private LocalDateTime lastMessageTime;

    private Long unreadCount;




}
