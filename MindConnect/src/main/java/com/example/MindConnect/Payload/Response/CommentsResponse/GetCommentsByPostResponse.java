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

public class GetCommentsByPostResponse {

    private UUID commentID;

    private String authorName;

    private String content;

    private LocalDateTime commentedAt;

    private boolean ownedByCurrentUser;


}
