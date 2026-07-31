package com.example.MindConnect.Payload.Response.PostsResponse;

import com.example.MindConnect.Enums.PostVisibility;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class GetPostsByVisibilityResponse {

    private UUID postID;

    private String authorName;

    private String content;

    private PostVisibility visibility;

    private LocalDateTime postedAt;

    private int likesCount;

    private int commentsCount;

    private boolean ownedByCurrentUser;

    private boolean likedByCurrentUser;
}
