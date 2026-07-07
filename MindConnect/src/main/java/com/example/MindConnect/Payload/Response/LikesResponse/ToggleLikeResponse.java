package com.example.MindConnect.Payload.Response.LikesResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ToggleLikeResponse {

    private UUID postID;

    private boolean liked;

    private Long likeCount;

    private String message;

}
