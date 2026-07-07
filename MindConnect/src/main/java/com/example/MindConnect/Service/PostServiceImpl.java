package com.example.MindConnect.Service;

import com.example.MindConnect.Entity.PostEntity;
import com.example.MindConnect.Entity.UserEntity;
import com.example.MindConnect.Enums.AccountStatus;
import com.example.MindConnect.Payload.Request.PostsRequest.CreatePostRequest;
import com.example.MindConnect.Payload.Request.PostsRequest.DeletePostRequest;
import com.example.MindConnect.Payload.Request.PostsRequest.UpdatePostByIdRequest;
import com.example.MindConnect.Payload.Response.PostsResponse.*;
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

public class PostServiceImpl {

    private final PostRepository postRepository;

    private final UserRepository userRepository;



    public CreatePostResponse createPost(CreatePostRequest request){

       String email = SecurityContextHolder.getContext().getAuthentication().getName();

       UserEntity user = userRepository.findByEmail(email)
               .orElseThrow(()->new RuntimeException("Authenticated user not found."));

       if(user.getStatus() != AccountStatus.ACTIVE){
           throw new RuntimeException("You are not allowed access to this feature");
       }

        PostEntity post = PostEntity.builder()
                .author(user)
                .content(request.getContent())
                .postedAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .visibility(request.getPostVisibility())
                .build();

        PostEntity new_post = postRepository.save(post);

        CreatePostResponse response = CreatePostResponse.builder()
                .authorName(user.getFirstName() + " " + user.getLastName())
                .content(new_post.getContent())
                .postID(new_post.getId())
                .message("Posted Successfully")
                .localDateTime(new_post.getPostedAt())
                .visibility(new_post.getVisibility())
                .build();


        return response;


    }

    public GetPostsByIdResponse getPostsById(UUID id){

       PostEntity post = postRepository.findById(id)
               .orElseThrow(()-> new RuntimeException("Post with entered ID does not exist!"));

       GetPostsByIdResponse response = GetPostsByIdResponse.builder()
               .postId(post.getId())
               .authorName(post.getAuthor().getFirstName() + " " + post.getAuthor().getLastName())
               .content(post.getContent())
               .postVisibility(post.getVisibility())
               .postedAt(post.getPostedAt())
               .updatedAt(post.getUpdatedAt())
               .likesCount(post.getLikes().size())
               .commentsCount(post.getComments().size())
               .build();
       return response;




    }


    public List<GetAllPostsResponse> getAllPosts(){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();



        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Authenticated user not found."));


        if(user.getStatus() != AccountStatus.ACTIVE){
            throw new RuntimeException("You must have an active account to access posts.");
        }

        List<PostEntity> allPosts = postRepository.findAll();

        List<GetAllPostsResponse> postsResponses = new ArrayList<>();

        for(PostEntity x: allPosts){
            GetAllPostsResponse response = GetAllPostsResponse.builder()
                    .postID(x.getId())
                    .authorName(x.getAuthor().getFirstName() + " " + x.getAuthor().getLastName())
                    .content(x.getContent())
                    .visibility(x.getVisibility())
                    .postedAt(x.getPostedAt())
                    .updatedAt(x.getUpdatedAt())
                    .likesCount(x.getLikes().size())
                    .commentsCount(x.getComments().size())
                    .build();
            postsResponses.add(response);
        }


        return postsResponses;


    }

    public List<GetPostsByUserResponse> getPostsByCurrentUser(){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Authenticated user not found."));

        if(user.getStatus() != AccountStatus.ACTIVE){
            throw new RuntimeException("You must have an active account to access posts.");
        }

        List<PostEntity> postsByUser = postRepository.findByAuthor(user);

        List<GetPostsByUserResponse> postsResponse = new ArrayList<>();

        for(PostEntity post: postsByUser){
            GetPostsByUserResponse response = GetPostsByUserResponse.builder()
                    .postID(post.getId())
                    .content(post.getContent())
                    .visibility(post.getVisibility())
                    .postedAt(post.getPostedAt())
                    .updatedAt(post.getUpdatedAt())
                    .likesCount(post.getLikes().size())
                    .commentsCount(post.getComments().size())
                    .build();

            postsResponse.add(response);

        }

        return postsResponse;


    }

    public UpdatePostsResponse updatePosts(UpdatePostByIdRequest request){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Authenticated user not found."));

        if((user.getStatus() != AccountStatus.ACTIVE)){

            throw new RuntimeException("You must have an active account to update posts");
        }


        PostEntity post = postRepository.findById(request.getPostID())
                .orElseThrow(()->new RuntimeException("Post not found"));



        if(post.getAuthor().getEmail().equals(email)){

            if(request.getNewContent() != null){
               if(request.getNewContent().isBlank()){
                   throw new RuntimeException("Post content cannot be blank");
               }
                post.setContent(request.getNewContent());
            }

            if(request.getVisibility() != null){
                post.setVisibility(request.getVisibility());

            }
            post.setUpdatedAt(LocalDateTime.now());
            postRepository.save(post);
        }else{
            throw new RuntimeException("You are not authorized to update this post");
        }

        UpdatePostsResponse response = UpdatePostsResponse.builder()
                .postID(post.getId())
                .authorName(post.getAuthor().getFirstName() + " " + post.getAuthor().getLastName())
                .content(post.getContent())
                .updatedAt(post.getUpdatedAt())
                .message("Post updated successfully")
                .build();

        return response;



    }

    public DeletePostResponse deletePosts(UUID id){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();


        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Authenticated user not found"));

        if(user.getStatus() != AccountStatus.ACTIVE){

            throw new RuntimeException("Your account must be active to delete posts.");
        }

        PostEntity post = postRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Post not found"));


        if(post.getAuthor().getEmail().equals(email)){

            postRepository.delete(post);
        }else{
            throw new RuntimeException("You are not authorized to delete this post");
        }

        DeletePostResponse response = DeletePostResponse.builder()
                .postID(post.getId())
                .deletedAt(LocalDateTime.now())
                .message("Post deleted successfully")
                .build();

        return response;

    }




}

