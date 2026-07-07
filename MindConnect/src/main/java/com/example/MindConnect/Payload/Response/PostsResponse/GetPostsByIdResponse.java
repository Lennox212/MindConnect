package com.example.MindConnect.Payload.Response.PostsResponse;

import com.example.MindConnect.Entity.CommentEntity;
import com.example.MindConnect.Entity.LikesEntity;
import com.example.MindConnect.Enums.PostVisibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class GetPostsByIdResponse {

    private UUID postId;

    private String authorName;

    private String content;

    private PostVisibility postVisibility;

    private LocalDateTime postedAt;

    private LocalDateTime updatedAt;

    private int likesCount;

    private int commentsCount;



}
