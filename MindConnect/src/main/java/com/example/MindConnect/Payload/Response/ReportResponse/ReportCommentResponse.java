package com.example.MindConnect.Payload.Response.ReportResponse;

import com.example.MindConnect.Enums.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReportCommentResponse {
    private UUID reportId;

    private UUID commentID;

    private String reportedBy;

    private String reason;

    private LocalDateTime reportedAt;

    private ReportStatus status;

    private String message;





}
