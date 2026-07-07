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
public class CreateCommentResponse {

    private UUID commentID;

    private UUID postID;

    private String authorName;

    private String content;

    private LocalDateTime commentedAt;

    private String message;


}
