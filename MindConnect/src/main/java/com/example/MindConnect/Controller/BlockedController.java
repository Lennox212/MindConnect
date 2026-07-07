package com.example.MindConnect.Controller;

import com.example.MindConnect.Payload.Request.BlockedRequest.BlockedUserRequest;
import com.example.MindConnect.Payload.Response.BlockedResponse.BlockUserResponse;
import com.example.MindConnect.Payload.Response.BlockedResponse.GetBlockedUsersResponse;
import com.example.MindConnect.Payload.Response.BlockedResponse.UnblockUserResponse;
import com.example.MindConnect.Service.BlockedServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/blocked")
@RestController
@RequiredArgsConstructor


public class BlockedController {

    private final BlockedServiceImpl blockedService;

    @PostMapping("/block-user")
    public BlockUserResponse blockUser(@RequestBody BlockedUserRequest request){
        return blockedService.blockUser(request);
    }

    @PostMapping("/unblock-user/{userId}")
    public UnblockUserResponse unblockUser(@PathVariable UUID userId){
        return blockedService.unblockUser(userId);
    }

    @GetMapping("/get-blocked-users")
    public List<GetBlockedUsersResponse> getBlockedUsers(){
        return blockedService.getBlockedUsers();
    }








}
