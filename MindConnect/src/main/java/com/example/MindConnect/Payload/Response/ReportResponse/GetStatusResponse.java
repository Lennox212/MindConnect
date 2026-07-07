package com.example.MindConnect.Payload.Response.ReportResponse;


import com.example.MindConnect.Enums.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class GetStatusResponse {

private UUID reportID;

private String reportedBy;

private UUID reportedPostID;

private UUID reportedCommentID;

private UUID reportedGroupID;

private UUID reportedMessageID;

private UUID reportedUserID;

private String reason;

private LocalDateTime reportedAt;

private ReportStatus status;





}
