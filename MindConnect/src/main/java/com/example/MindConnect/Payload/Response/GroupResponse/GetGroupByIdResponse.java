package com.example.MindConnect.Payload.Response.GroupResponse;

import com.example.MindConnect.Enums.GroupVisibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class GetGroupByIdResponse {

    private UUID groupID;

    private String groupName;

    private String description;

    private String ownerName;

    private int memberCount;

    private GroupVisibility groupVisibility;
}
