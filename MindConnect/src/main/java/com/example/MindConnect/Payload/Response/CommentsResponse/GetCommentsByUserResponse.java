package com.example.MindConnect.Payload.Response.CommentsResponse;

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

public class GetCommentsByUserResponse {

    private String authorName;

    private UUID commentID;

    private UUID postID;

    private String content;

    private LocalDateTime commentedAt;

    private LocalDateTime updatedAt;

}
