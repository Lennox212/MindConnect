package com.example.MindConnect.Payload.Request.ReportRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@Data
@NoArgsConstructor


public class ReportMessageRequest {

    private UUID messageID;

    private String reason;
}
