package com.example.MindConnect.Service;

import com.example.MindConnect.Entity.CommentEntity;
import com.example.MindConnect.Entity.PostEntity;
import com.example.MindConnect.Entity.UserEntity;
import com.example.MindConnect.Enums.AccountStatus;
import com.example.MindConnect.Payload.Request.CommentsRequest.CreateCommentRequest;
import com.example.MindConnect.Payload.Request.CommentsRequest.UpdateCommentRequest;
import com.example.MindConnect.Payload.Response.CommentsResponse.*;

import com.example.MindConnect.Repository.CommentsRepository;
import com.example.MindConnect.Repository.PostRepository;
import com.example.MindConnect.Repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Data
@RequiredArgsConstructor

public class CommentServiceImpl {

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final CommentsRepository commentsRepository;


    public CreateCommentResponse createComment(CreateCommentRequest request){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Authenticated user not found"));


        if(user.getStatus() != AccountStatus.ACTIVE){
            throw new RuntimeException("You are not allowed to use this feature.");
        }

        PostEntity post = postRepository.findById(request.getPostID())
                .orElseThrow(()->new RuntimeException("Post with entered ID does not exist!"));

        if(request.getContent() == null){
            throw new RuntimeException("Comment content cannot be empty");
        }



        CommentEntity comment = CommentEntity.builder()
                .post(post)
                .comment(request.getContent())
                .commentedAt(LocalDateTime.now())
                .commentedBy(user)
                .build();

        CommentEntity savedComment = commentsRepository.save(comment);

        CreateCommentResponse response = CreateCommentResponse.builder()
                .postID(request.getPostID())
                .commentID(savedComment.getId())
                .authorName(savedComment.getCommentedBy().getFirstName() + " " + savedComment.getCommentedBy().getLastName())
                .content(savedComment.getComment())
                .commentedAt(savedComment.getCommentedAt())
                .message("Comment posted successfully")
                .build();
        return response;

    }

    public List<GetCommentsByPostResponse> getCommentsByPost(UUID postID){

        PostEntity post = postRepository.findById(postID).orElseThrow(()-> new RuntimeException("Post not found"));

         List<CommentEntity> comments = commentsRepository.findByPost(post);

         List<GetCommentsByPostResponse> allComments = new ArrayList<>();

         for(CommentEntity comment: comments) {
             GetCommentsByPostResponse response = GetCommentsByPostResponse.builder()
                     .commentID(comment.getId())
                     .authorName(comment.getCommentedBy().getFirstName() + " " + comment.getCommentedBy().getLastName())
                     .content(comment.getComment())
                     .commentedAt(comment.getCommentedAt())
                     .build();

             allComments.add(response);
         }
         return allComments;


    }


    public List<GetCommentsByUserResponse> getCommentsByUser(){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Authenticated user not found"));

        if(user.getStatus() != AccountStatus.ACTIVE){
            throw new RuntimeException("You are not allowed to use this feature");

        }

        List<CommentEntity> comments_by_user = commentsRepository.findByCommentedBy(user);

        List<GetCommentsByUserResponse> comments = new ArrayList<>();

        for(CommentEntity comment: comments_by_user){
            GetCommentsByUserResponse response = GetCommentsByUserResponse.builder()
                    .authorName(comment.getCommentedBy().getFirstName() + " " + comment.getCommentedBy().getLastName())
                    .commentID(comment.getId())
                    .postID(comment.getPost().getId())
                    .content(comment.getComment())
                    .commentedAt(comment.getCommentedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .build();

            comments.add(response);

        }

        return comments;


    }


    public UpdateCommentResponse updateComment(UpdateCommentRequest request){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();


        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Authenticated user not found"));

        if(user.getStatus() != AccountStatus.ACTIVE){
            throw new RuntimeException("You are not allowed to use this feature");
        }

        CommentEntity comment = commentsRepository.findById(request.getCommentId())
                .orElseThrow(()->new RuntimeException("Comment not found"));

        if(comment.getCommentedBy().getEmail().equals(email)){

            if(request.getNewContent() == null) {

                throw new RuntimeException("Comment cannot be empty");
            }

                comment.setComment(request.getNewContent());
                comment.setUpdatedAt(LocalDateTime.now());
                commentsRepository.save(comment);
            }
        else{
            throw new RuntimeException("You are not allowed to edit this comment.");
        }

        UpdateCommentResponse response = UpdateCommentResponse.builder()
                .commentId(comment.getId())
                .authorName(comment.getCommentedBy().getFirstName()+ " " + comment.getCommentedBy().getLastName())
                .content(comment.getComment())
                .updateAt(comment.getUpdatedAt())
                .message("Comment updated successfully")
                .build();

        return response;


    }

    public DeleteCommentResponse deleteComment(UUID commentID){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Authenticated user not found"));


        if(user.getStatus() != AccountStatus.ACTIVE){

            throw new RuntimeException("You are not allowed to use this feature");
        }

        CommentEntity comment = commentsRepository.findById(commentID)
                .orElseThrow(()->new RuntimeException("Comment not found"));


            if(comment.getCommentedBy().getEmail().equals(email)){

                commentsRepository.delete(comment);
            }else {
                throw new RuntimeException("You are not allowed to delete this comment.");
            }
        DeleteCommentResponse response = DeleteCommentResponse.builder()
                .commentID(commentID)
                .deletedAt(LocalDateTime.now())
                .message("Comment deleted successfully")
                .build();

        return response;




    }




}
