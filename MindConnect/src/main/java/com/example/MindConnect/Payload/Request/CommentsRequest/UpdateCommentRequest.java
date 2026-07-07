package com.example.MindConnect.Payload.Request.CommentsRequest;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class UpdateCommentRequest {


    private UUID commentId;

    private String newContent;
}
