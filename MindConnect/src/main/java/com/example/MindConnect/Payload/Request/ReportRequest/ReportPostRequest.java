package com.example.MindConnect.Payload.Request.ReportRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data

public class ReportPostRequest {

    private UUID postID;

    private String reason;
}
