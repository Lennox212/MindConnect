package com.example.MindConnect.Payload.Response.GroupResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class JoinGroupResponse {

    private UUID groupID;

    private String groupName;

    private UUID userID;

    private String userName;

    private int memberCount;

    private String message;
}
