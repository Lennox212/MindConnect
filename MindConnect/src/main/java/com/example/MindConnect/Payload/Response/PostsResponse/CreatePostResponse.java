package com.example.MindConnect.Payload.Response.PostsResponse;

import com.example.MindConnect.Entity.UserEntity;
import com.example.MindConnect.Enums.PostVisibility;
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
public class CreatePostResponse {

    private UUID postID;

    private String authorName;

    private PostVisibility visibility;

    private String content;

    private LocalDateTime localDateTime;

    private String message;

}
