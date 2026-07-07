package com.example.MindConnect.Payload.Response.GroupResponse;

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
public class DeleteGroupResponse {
    private UUID groupID;

    private String groupName;

    private String deletedBy;

    private LocalDateTime deletedAt;

    private String message;

}
