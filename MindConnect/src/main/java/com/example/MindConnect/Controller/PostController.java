package com.example.MindConnect.Controller;

import com.example.MindConnect.Payload.Request.PostsRequest.CreatePostRequest;
import com.example.MindConnect.Payload.Request.PostsRequest.UpdatePostByIdRequest;
import com.example.MindConnect.Payload.Response.PostsResponse.*;
import com.example.MindConnect.Service.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor



public class PostController {

    private final PostServiceImpl postService;

    @PostMapping("/create-post")
    public CreatePostResponse create_post(@RequestBody CreatePostRequest request){

        return postService.createPost(request);

    }

    @GetMapping("/{id}")
    public GetPostsByIdResponse getPostsById(@PathVariable UUID id){

        return postService.getPostsById(id);
    }

    @GetMapping("/feed")
    public List<GetPostsByVisibilityResponse> getFeedPosts() {
        return postService.getFeedPosts();
    }

    @GetMapping
    public List<GetAllPostsResponse> getAllPosts(){
        return postService.getAllPosts();
    }

    @GetMapping("/my-posts")
    List<GetPostsByUserResponse> getPostsByUser(){
        return postService.getPostsByCurrentUser();
    }

    @PutMapping("/update-post")
    public UpdatePostsResponse updatePost(@RequestBody UpdatePostByIdRequest request){

        return postService.updatePosts(request);
    }

    @DeleteMapping("/delete-post/{id}")
    public DeletePostResponse deletePost(@PathVariable UUID id){

        return postService.deletePosts(id);
    }





















}
