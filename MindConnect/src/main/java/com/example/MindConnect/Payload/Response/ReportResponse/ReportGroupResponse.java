package com.example.MindConnect.Payload.Response.ReportResponse;


import com.example.MindConnect.Enums.ReportStatus;
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

public class ReportGroupResponse {

    private UUID reportID;

    private UUID groupID;

    private String reportedBy;

    private String reason;

    private LocalDateTime reportedAt;

    private ReportStatus status;

    private String message;








}
