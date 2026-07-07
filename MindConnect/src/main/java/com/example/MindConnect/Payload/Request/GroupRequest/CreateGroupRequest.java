package com.example.MindConnect.Payload.Request.GroupRequest;


import com.example.MindConnect.Entity.GroupEntity;
import com.example.MindConnect.Enums.GroupVisibility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class CreateGroupRequest {

    private String groupName;

    private String description;

    private GroupVisibility groupVisibility;

}
