package com.example.MindConnect.Payload.Request.PostsRequest;

import com.example.MindConnect.Enums.PostVisibility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class UpdatePostByIdRequest {

    private UUID postID;

    private PostVisibility visibility;

    private String newContent;
}
