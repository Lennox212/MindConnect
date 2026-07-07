package com.example.MindConnect.Payload.Request.GroupRequest;

import com.example.MindConnect.Enums.GroupVisibility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data


public class UpdateGroupRequest {
    private UUID id;

    private String newDescription;

    private String newGroupName;

    private GroupVisibility newGroupVisibility;

}
