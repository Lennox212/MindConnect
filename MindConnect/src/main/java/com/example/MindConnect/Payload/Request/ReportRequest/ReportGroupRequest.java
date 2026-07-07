package com.example.MindConnect.Payload.Request.ReportRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ReportGroupRequest {

    private UUID groupID;

    private String reason;







}
