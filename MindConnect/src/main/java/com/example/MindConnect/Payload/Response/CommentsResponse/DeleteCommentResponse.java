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

public class DeleteCommentResponse {

    private UUID commentID;

    private LocalDateTime deletedAt;

    private String message;





}
