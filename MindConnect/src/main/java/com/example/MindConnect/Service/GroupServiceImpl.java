package com.example.MindConnect.Service;

import com.example.MindConnect.Entity.GroupEntity;
import com.example.MindConnect.Entity.UserEntity;
import com.example.MindConnect.Enums.AccountStatus;
import com.example.MindConnect.Enums.GroupVisibility;
import com.example.MindConnect.Payload.Request.GroupRequest.CreateGroupRequest;
import com.example.MindConnect.Payload.Request.GroupRequest.UpdateGroupRequest;
import com.example.MindConnect.Payload.Response.GroupResponse.*;
import com.example.MindConnect.Repository.GroupsRepository;
import com.example.MindConnect.Repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Data
@RequiredArgsConstructor

public class GroupServiceImpl {

    private final GroupsRepository groupsRepository;

    private final UserRepository userRepository;



    public CreateGroupResponse createGroup(CreateGroupRequest request){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Authenticated user not found"));

        if(user.getStatus() != AccountStatus.ACTIVE){
            throw new RuntimeException("You are not allowed to create a group at this time");
        }

        if(request.getGroupName() == null || request.getGroupName().isBlank()
                || request.getDescription() == null ||
                request.getDescription().isBlank() ||
                request.getGroupVisibility() == null){

            throw new RuntimeException("Field must not be blank");
        }

        if(groupsRepository.existsByGroupName(request.getGroupName())){
            throw new RuntimeException("Group with this name already exists");
        }

        GroupEntity group = GroupEntity.builder()
                .groupName(request.getGroupName())
                .description(request.getDescription())
                .owner(user)
                .members(new HashSet<>(Set.of(user)))
                .groupVisibility(request.getGroupVisibility())
                .createdAt(LocalDateTime.now())
                .build();

        GroupEntity saved_group = groupsRepository.save(group);


        CreateGroupResponse response = CreateGroupResponse.builder()
                .groupName(saved_group.getGroupName())
                .groupID(saved_group.getId())
                .ownerName(saved_group.getOwner().getFirstName() + " " + saved_group.getOwner().getLastName())
                .description(saved_group.getDescription())
                .groupVisibility(saved_group.getGroupVisibility())
                .createdAt(saved_group.getCreatedAt())
                .message("Group created successfully")
                .build();

        return response;

    }

    public List<GetPublicGroupsResponse> getPublicGroups(){


        List<GroupEntity> groups = groupsRepository.findByGroupVisibility(GroupVisibility.PUBLIC);

        List<GetPublicGroupsResponse> public_groups = new ArrayList<>();

        for(GroupEntity x:groups){

            GetPublicGroupsResponse response = GetPublicGroupsResponse.builder()
                    .groupID(x.getId())
                    .groupName(x.getGroupName())
                    .description(x.getDescription())
                    .ownerName(x.getOwner().getFirstName() + " " + x.getOwner().getLastName())
                    .groupVisibility(x.getGroupVisibility())
                    .memberCount(x.getMembers().size())
                    .build();

            public_groups.add(response);

        }

        return public_groups;

    }

    public GetGroupByIdResponse getGroupById(UUID id){

        GroupEntity group = groupsRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Group not found"));

        GetGroupByIdResponse response = GetGroupByIdResponse.builder()
                .groupID(group.getId())
                .groupName(group.getGroupName())
                .ownerName(group.getOwner().getFirstName() + " " + group.getOwner().getLastName())
                .description(group.getDescription())
                .memberCount(group.getMembers().size())
                .groupVisibility(group.getGroupVisibility())
                .build();
        return response;


    }

    public JoinGroupResponse joinGroup(UUID id){


        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("Authenticated user not found"));

        if (user.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("You are not allowed to join groups at this time");
        }

        GroupEntity group = groupsRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Group not found"));


        if(group.getMembers().contains(user)){
            throw new RuntimeException("User already in group");
        }

        group.getMembers().add(user);

        GroupEntity joined_group = groupsRepository.save(group);


        JoinGroupResponse response = JoinGroupResponse.builder()
                .groupID(joined_group.getId())
                .groupName(joined_group.getGroupName())
                .userName(user.getFirstName() + " " + user.getLastName())
                .userID(user.getId())
                .memberCount(joined_group.getMembers().size())
                .message("Group joined successfully")
                .build();

        return response;
    }

    public LeaveGroupResponse leaveGroup(UUID id){

        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("Authenticated user not found"));

        if (user.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("You are not allowed to join groups at this time");
        }

        GroupEntity group = groupsRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Group not found"));

        if(!group.getMembers().contains(user)){

            throw new RuntimeException("Must be a member to leave a group");
        }

        if(group.getOwner().getEmail().equals(email)){
            throw new RuntimeException("Group owner cannot leave their own group");
        }

        group.getMembers().remove(user);

        GroupEntity saved_group = groupsRepository.save(group);

        LeaveGroupResponse response = LeaveGroupResponse.builder()
                .groupID(saved_group.getId())
                .groupName(saved_group.getGroupName())
                .userID(user.getId())
                .userName(user.getFirstName() + " " + user.getLastName())
                .memberCount(saved_group.getMembers().size())
                .message("Group left successfully")
                .build();

        return response;





    }

    public UpdateGroupResponse updatedGroup(UpdateGroupRequest request){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("Authenticated user not found"));

        if (user.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("You are not allowed to update groups at this time");
        }

        GroupEntity group = groupsRepository.findById(request.getId())
                .orElseThrow(()-> new RuntimeException("Group not found"));

        if(!group.getOwner().getEmail().equals(email)){

            throw new RuntimeException("You are not allowed to update groups info");
        }

        if(request.getNewGroupName() == null || request.getNewGroupName().isBlank()
                || request.getNewDescription() == null ||
                request.getNewDescription().isBlank() ||
                request.getNewGroupVisibility() == null){

            throw new RuntimeException("Field must not be blank");
        }

        if (!group.getGroupName().equals(request.getNewGroupName())) {

            if (groupsRepository.existsByGroupName(request.getNewGroupName())) {
                throw new RuntimeException("Group with this name already exists");
            }
        }
        group.setGroupName(request.getNewGroupName());
        group.setDescription(request.getNewDescription());
        group.setGroupVisibility(request.getNewGroupVisibility());

        GroupEntity updated_group = groupsRepository.save(group);

        UpdateGroupResponse response = UpdateGroupResponse.builder()
                .groupID(updated_group.getId())
                .groupName(updated_group.getGroupName())
                .description(updated_group.getDescription())
                .ownerName(updated_group.getOwner().getFirstName() + " " + updated_group.getOwner().getLastName())
                .groupVisibility(updated_group.getGroupVisibility())
                .updatedAt(updated_group.getUpdatedAt())
                .message("Group updated successfully")
                .build();

        return response;

    }


    public DeleteGroupResponse deleteGroup(UUID id){

        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Authenticated user not found"));

        if(user.getStatus() != AccountStatus.ACTIVE){

            throw new RuntimeException("You are not allowed to delete this group");
        }

        GroupEntity group = groupsRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Group not found"));

        if(!group.getOwner().getEmail().equals(email)){

            throw new RuntimeException("You are not allowed to delete this group");
        }

        UUID groupID = group.getId();
        String groupName = group.getGroupName();
        String deletedBy = group.getOwner().getFirstName() + " " + group.getOwner().getLastName();
        LocalDateTime deletedAt = LocalDateTime.now();


        groupsRepository.delete(group);


         DeleteGroupResponse response = DeleteGroupResponse.builder()
                 .groupID(groupID)
                 .groupName(groupName)
                 .deletedBy(deletedBy)
                 .deletedAt(deletedAt)
                 .message("Group successfully deleted")
                 .build();
         return response;
    }












}
