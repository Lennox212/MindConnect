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


public class EditMessageResponse {

    private UUID messageID;

    private String content;

    private LocalDateTime updatedAt;

    private String messageStatus;




}
