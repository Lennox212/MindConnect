package com.example.MindConnect.Controller;

import com.example.MindConnect.Payload.Response.LikesResponse.ToggleLikeResponse;
import com.example.MindConnect.Service.LikesServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor

public class LikesController {

    private final LikesServiceImpl likesService;

    @PostMapping("/like-post/{postID}")
    public ToggleLikeResponse likePost(@PathVariable UUID postID){

        return likesService.likePost(postID);

    }






}
