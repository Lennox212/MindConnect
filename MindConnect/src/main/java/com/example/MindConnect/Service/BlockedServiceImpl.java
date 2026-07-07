package com.example.MindConnect.Service;

import com.example.MindConnect.Entity.BlockedEntity;
import com.example.MindConnect.Entity.UserEntity;
import com.example.MindConnect.Enums.AccountStatus;
import com.example.MindConnect.Payload.Request.BlockedRequest.BlockedUserRequest;
import com.example.MindConnect.Payload.Response.BlockedResponse.BlockUserResponse;
import com.example.MindConnect.Payload.Response.BlockedResponse.GetBlockedUsersResponse;
import com.example.MindConnect.Payload.Response.BlockedResponse.UnblockUserResponse;
import com.example.MindConnect.Repository.BlocksRepository;
import com.example.MindConnect.Repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Data

public class BlockedServiceImpl {
    private final UserRepository userRepository;

    private final BlocksRepository blocksRepository;




public BlockUserResponse blockUser(BlockedUserRequest request){

    String email = SecurityContextHolder.getContext()
            .getAuthentication().getName();

    UserEntity blockedBy = userRepository.findByEmail(email)
            .orElseThrow(()->new RuntimeException("Authenticated user not found"));

    if(blockedBy.getStatus() != AccountStatus.ACTIVE){

        throw new RuntimeException("You are not allowed to block any users at this time");
    }

    UserEntity blockedUser = userRepository.findById(request.getBlockedUserID())
            .orElseThrow(()->new RuntimeException("User to be blocked not found"));


    if(blockedUser.getEmail().equals(email)){
        throw new RuntimeException("You are not allowed to block yourself");
    }

    if(blocksRepository.existsByBlockedByAndBlockedUser(blockedBy, blockedUser)){

        throw new RuntimeException("You have already blocked this user");
    }

    BlockedEntity blocked_user = BlockedEntity.builder()
            .blockedUser(blockedUser)
            .blockedBy(blockedBy)
            .blockedAt(LocalDateTime.now())
            .motive(request.getMotive())
            .build();

    BlockedEntity saved_block = blocksRepository.save(blocked_user);

    BlockUserResponse response = BlockUserResponse.builder()
            .blockID(saved_block.getId())
            .blockedUserID(saved_block.getBlockedUser().getId())
            .blockedUserName(saved_block.getBlockedUser().getFirstName() + " " + saved_block.getBlockedUser().getLastName())
            .blockedBy(saved_block.getBlockedBy().getFirstName() + " " + saved_block.getBlockedBy().getLastName())
            .blockedAt(saved_block.getBlockedAt())
            .motive(saved_block.getMotive())
            .message("User blocked successfully")
            .build();

    return response;

}

public UnblockUserResponse unblockUser(UUID userId){

    String email = SecurityContextHolder.getContext()
            .getAuthentication().getName();

    UserEntity blockedBy = userRepository.findByEmail(email)
            .orElseThrow(()->new RuntimeException("Authenticated user not found"));

    if(blockedBy.getStatus() != AccountStatus.ACTIVE){

        throw new RuntimeException("You are not allowed to unblock any users at this time");
    }


    UserEntity unblockUser = userRepository.findById(userId)
            .orElseThrow(()->new RuntimeException("User to be unblocked not found"));

    BlockedEntity blockRecord = blocksRepository.findByBlockedByAndBlockedUser(blockedBy,unblockUser)
            .orElseThrow(()->new RuntimeException("No record found"));

    blocksRepository.delete(blockRecord);

    UnblockUserResponse response = UnblockUserResponse.builder()
            .unblockedUserID(blockRecord.getBlockedUser().getId())
            .unblockedUserName(blockRecord.getBlockedUser().getFirstName() + " " + blockRecord.getBlockedUser().getLastName())
            .unblockedBy(blockRecord.getBlockedBy().getFirstName() + " " + blockRecord.getBlockedBy().getLastName())
            .unblockedAt(LocalDateTime.now())
            .message("User unblocked successfully")
            .build();

    return response;
}

public List<GetBlockedUsersResponse> getBlockedUsers(){

    String email = SecurityContextHolder.getContext()
            .getAuthentication().getName();

    UserEntity blockedBy = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("Authenticated user not found"));

    if(blockedBy.getStatus() != AccountStatus.ACTIVE){
        throw new RuntimeException("You are not allowed to view blocked users at this time");
    }

    List<BlockedEntity> blocked_users = blocksRepository.findByBlockedBy(blockedBy);

    List<GetBlockedUsersResponse> foundBlockedUsers = new ArrayList<>();

    for(BlockedEntity block: blocked_users){
        GetBlockedUsersResponse response = GetBlockedUsersResponse.builder()
                .blockID(block.getId())
                .blockedUserID(block.getBlockedUser().getId())
                .blockedUserName(block.getBlockedUser().getFirstName() + " " + block.getBlockedUser().getLastName())
                .blockedAt(block.getBlockedAt())
                .motive(block.getMotive())
                .build();

        foundBlockedUsers.add(response);

    }

    return foundBlockedUsers;




}















}
