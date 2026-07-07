package com.example.MindConnect.Payload.Request.ReportRequest;

import com.example.MindConnect.Enums.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ReviewReportRequest {

    private UUID reportID;

    private String adminNote;

    private ReportStatus status;


}
