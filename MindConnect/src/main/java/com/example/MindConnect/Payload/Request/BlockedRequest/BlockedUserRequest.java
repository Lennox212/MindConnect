package com.example.MindConnect.Payload.Request.BlockedRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class BlockedUserRequest {

    private UUID blockedUserID;

    private String motive;


}
