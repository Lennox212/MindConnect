package com.example.MindConnect.Payload.Response.ReportResponse;

import com.example.MindConnect.Enums.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ReviewReportResponse {

    private UUID reportID;

    private String reviewedBy;

    private LocalDateTime reviewedAt;

    private String adminNote;

    private ReportStatus status;

    private String message;




}
