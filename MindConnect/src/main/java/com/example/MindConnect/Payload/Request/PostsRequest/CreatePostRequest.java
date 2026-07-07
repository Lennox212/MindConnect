package com.example.MindConnect.Payload.Request.PostsRequest;

import com.example.MindConnect.Enums.PostVisibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePostRequest {


    private String content;

    private PostVisibility postVisibility;
}
