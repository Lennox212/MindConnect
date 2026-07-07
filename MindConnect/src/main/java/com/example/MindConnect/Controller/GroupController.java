package com.example.MindConnect.Controller;

import com.example.MindConnect.Payload.Request.GroupRequest.CreateGroupRequest;
import com.example.MindConnect.Payload.Request.GroupRequest.UpdateGroupRequest;
import com.example.MindConnect.Payload.Response.GroupResponse.*;
import com.example.MindConnect.Service.GroupServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor

public class GroupController {

    private final GroupServiceImpl groupService;

    @PostMapping("/create-group")
    public CreateGroupResponse createGroup(@RequestBody CreateGroupRequest request)
    {

        return groupService.createGroup(request);
    }

    @GetMapping("/get-public-groups")
    public List<GetPublicGroupsResponse> getPublicGroups(){

        return groupService.getPublicGroups();
    }

    @GetMapping("/get-group-by-id/{id}")
    public GetGroupByIdResponse getGroupById(@PathVariable UUID id){

        return groupService.getGroupById(id);

    }

    @PutMapping("/join-group/{id}")
    public JoinGroupResponse joinGroup(@PathVariable UUID id){
        return groupService.joinGroup(id);
    }

    @PutMapping("/leave-group/{id}")
    public LeaveGroupResponse leaveGroup(@PathVariable UUID id){
        return groupService.leaveGroup(id);
    }

    @PutMapping("/update-group")
    public UpdateGroupResponse updatedGroup(@RequestBody UpdateGroupRequest request){

        return groupService.updatedGroup(request);
    }

    @DeleteMapping("/delete-group/{id}")
    public DeleteGroupResponse deleteGroup(@PathVariable UUID id){

        return groupService.deleteGroup(id);
    }

}




