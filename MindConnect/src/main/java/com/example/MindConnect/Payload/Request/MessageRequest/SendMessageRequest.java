package com.example.MindConnect.Payload.Request.MessageRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SendMessageRequest {

    private UUID recipientID;

    private String content;



}
