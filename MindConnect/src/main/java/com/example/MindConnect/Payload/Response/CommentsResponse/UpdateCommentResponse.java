package com.example.MindConnect.Payload.Response.CommentsResponse;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor


public class UpdateCommentResponse {

    private UUID commentId;

    private String authorName;

    private String content;

    private LocalDateTime updateAt;

    private String message;



}
