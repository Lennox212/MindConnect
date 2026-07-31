package com.example.MindConnect.Service;

import com.example.MindConnect.CustomExceptions.*;
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
                .orElseThrow(()->new UserNotFoundException("Authenticated user not found"));


        if(user.getStatus() != AccountStatus.ACTIVE){
            throw new AccountInactiveException("You are not allowed to use this feature.");
        }

        PostEntity post = postRepository.findById(request.getPostID())
                .orElseThrow(()->new PostNotFoundException("Post with entered ID does not exist!"));

        if(request.getContent() == null || request.getContent().isBlank()){
            throw new BlankFieldException("Comment content cannot be empty");
        }



        CommentEntity comment = CommentEntity.builder()
                .post(post)
                .content(request.getContent().trim())
                .commentedAt(LocalDateTime.now())
                .commentedBy(user)
                .build();


        CommentEntity savedComment = commentsRepository.save(comment);


        CreateCommentResponse response = CreateCommentResponse.builder()
                .postID(request.getPostID())
                .commentID(savedComment.getId())
                .authorName(savedComment.getCommentedBy().getFirstName() + " " + savedComment.getCommentedBy().getLastName())
                .content(savedComment.getContent())
                .commentedAt(savedComment.getCommentedAt())
                .ownedByCurrentUser(true)
                .message("Comment posted successfully")
                .build();
        return response;

    }

    public List<GetCommentsByPostResponse> getCommentsByPost(UUID postID){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        PostEntity post = postRepository.findById(postID).orElseThrow(()-> new PostNotFoundException("Post not found"));

         List<CommentEntity> comments = commentsRepository.findByPostOrderByCommentedAtAsc(post);

         List<GetCommentsByPostResponse> allComments = new ArrayList<>();



         for(CommentEntity comment: comments) {

             boolean ownedByCurrentUser = comment.getCommentedBy().getEmail().equals(email);

                     GetCommentsByPostResponse response = GetCommentsByPostResponse.builder()
                     .commentID(comment.getId())
                     .authorName(comment.getCommentedBy().getFirstName() + " " + comment.getCommentedBy().getLastName())
                     .content(comment.getContent())
                     .ownedByCurrentUser(ownedByCurrentUser)
                     .commentedAt(comment.getCommentedAt())
                     .build();

             allComments.add(response);
         }
         return allComments;


    }


    public List<GetCommentsByUserResponse> getCommentsByUser(){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("Authenticated user not found"));

        if(user.getStatus() != AccountStatus.ACTIVE){
            throw new AccountInactiveException("You are not allowed to use this feature");

        }

        List<CommentEntity> userComments = commentsRepository.findByCommentedBy(user);

        List<GetCommentsByUserResponse> comments = new ArrayList<>();

        for(CommentEntity comment: userComments){
            GetCommentsByUserResponse response = GetCommentsByUserResponse.builder()
                    .authorName(comment.getCommentedBy().getFirstName() + " " + comment.getCommentedBy().getLastName())
                    .commentID(comment.getId())
                    .postID(comment.getPost().getId())
                    .content(comment.getContent())
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
                .orElseThrow(()->new UserNotFoundException("Authenticated user not found"));

        if(user.getStatus() != AccountStatus.ACTIVE){
            throw new AccountInactiveException("You are not allowed to use this feature");
        }

        CommentEntity comment = commentsRepository.findById(request.getCommentID())
                .orElseThrow(()->new CommentNotFoundException("Comment not found"));

        if(comment.getCommentedBy().getEmail().equals(email)){

            if(request.getNewContent() == null || request.getNewContent().isBlank()) {

                throw new BlankFieldException("Comment cannot be empty");
            }

                comment.setContent(request.getNewContent().trim());
                comment.setUpdatedAt(LocalDateTime.now());
                commentsRepository.save(comment);
            }
        else{
            throw new NotAuthorizedException("You are not allowed to edit this comment.");
        }

        UpdateCommentResponse response = UpdateCommentResponse.builder()
                .commentID(comment.getId())
                .authorName(comment.getCommentedBy().getFirstName()+ " " + comment.getCommentedBy().getLastName())
                .content(comment.getContent())
                .updateAt(comment.getUpdatedAt())
                .message("Comment updated successfully")
                .build();

        return response;


    }

    public DeleteCommentResponse deleteComment(UUID commentID){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("Authenticated user not found"));


        if(user.getStatus() != AccountStatus.ACTIVE){

            throw new AccountInactiveException("You are not allowed to use this feature");
        }

        CommentEntity comment = commentsRepository.findById(commentID)
                .orElseThrow(()->new CommentNotFoundException("Comment not found"));


            if(comment.getCommentedBy().getEmail().equals(email)){

                commentsRepository.delete(comment);
            }else {
                throw new NotAuthorizedException("You are not allowed to delete this comment.");
            }
        DeleteCommentResponse response = DeleteCommentResponse.builder()
                .commentID(commentID)
                .deletedAt(LocalDateTime.now())
                .message("Comment deleted successfully")
                .build();

        return response;




    }




}
