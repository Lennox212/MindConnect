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

public class UpdatePostsResponse {

    private UUID postID;

    private String authorName;

    private String content;

    private PostVisibility visibility;

    private LocalDateTime updatedAt;

    private String message;

}
