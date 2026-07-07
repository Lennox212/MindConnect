package com.example.MindConnect.Payload.Response.GroupResponse;

import com.example.MindConnect.Enums.GroupVisibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class CreateGroupResponse {

    private String groupName;

    private UUID groupID;

    private String description;

    private String ownerName;

    private GroupVisibility groupVisibility;

    private LocalDateTime createdAt;

    private String message;




}
