package com.example.MindConnect.Payload.Request.ReportRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class ReportCommentRequest {

    private UUID commentID;

    private String reason;

}
