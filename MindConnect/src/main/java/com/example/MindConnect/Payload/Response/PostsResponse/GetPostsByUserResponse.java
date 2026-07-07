package com.example.MindConnect.Payload.Response.PostsResponse;

import com.example.MindConnect.Enums.PostVisibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class GetPostsByUserResponse {


    private UUID postID;

    private String content;

    private PostVisibility visibility;

    private LocalDateTime postedAt;

    private LocalDateTime updatedAt;

    private int likesCount;

    private int commentsCount;
}
