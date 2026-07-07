package com.example.MindConnect.Controller;

import com.example.MindConnect.Payload.Request.CommentsRequest.CreateCommentRequest;
import com.example.MindConnect.Payload.Request.CommentsRequest.UpdateCommentRequest;
import com.example.MindConnect.Payload.Response.CommentsResponse.*;
import com.example.MindConnect.Service.CommentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor


public class CommentController {

    private final CommentServiceImpl commentService;

    @PostMapping("/create-comment")
    public CreateCommentResponse createComment(@RequestBody CreateCommentRequest request){

        return commentService.createComment(request);

    }

    @GetMapping("/post/{id}")
    public List<GetCommentsByPostResponse> getCommentsByPost(@PathVariable UUID id){

        return commentService.getCommentsByPost(id);
    }

    @GetMapping("/my-comments")
    public List<GetCommentsByUserResponse> getCommentsByUser(){

        return commentService.getCommentsByUser();
    }


    @PutMapping("/update-comment")
    public UpdateCommentResponse updateComment(@RequestBody UpdateCommentRequest request){

        return commentService.updateComment(request);
    }

    @DeleteMapping("/delete/{id}")
    public DeleteCommentResponse deleteComment(@PathVariable UUID id){

        return commentService.deleteComment(id);
    }




}
