package com.example.MindConnect.Payload.Request.CommentsRequest;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CreateCommentRequest {

    private UUID postID;

    private String content;
}
