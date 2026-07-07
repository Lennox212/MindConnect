package com.example.MindConnect.Service;


import com.example.MindConnect.Entity.LikesEntity;
import com.example.MindConnect.Entity.PostEntity;
import com.example.MindConnect.Entity.UserEntity;
import com.example.MindConnect.Enums.AccountStatus;
import com.example.MindConnect.Payload.Response.LikesResponse.ToggleLikeResponse;
import com.example.MindConnect.Repository.LikesRepository;
import com.example.MindConnect.Repository.PostRepository;
import com.example.MindConnect.Repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Data
@RequiredArgsConstructor
public class LikesServiceImpl {

    private final PostRepository postRepository;

    private final LikesRepository likesRepository;

    private final UserRepository userRepository;



    public ToggleLikeResponse likePost(UUID postID){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Authenticated user not found"));

        if(user.getStatus() != AccountStatus.ACTIVE){
            throw new RuntimeException("You are not allowed to use this feature.");
        }

        PostEntity post = postRepository.findById(postID)
                .orElseThrow(()->new RuntimeException("Post with entered ID does not exist!"));


        Optional<LikesEntity> existingLike = likesRepository
                    .findByLikedByAndPostLiked(user,post);

            if(existingLike.isPresent()){

                likesRepository.delete(existingLike.get());

                long count = likesRepository.countByPostLiked(post);

                return ToggleLikeResponse.builder()
                        .postID(postID)
                        .liked(false)
                        .likeCount(count)
                        .message("Post unliked")
                        .build();
            }

            LikesEntity likes = LikesEntity.builder()
                    .likedBy(user)
                    .postLiked(post)
                    .timeLiked(LocalDateTime.now())
                    .build();

            likesRepository.save(likes);

            long count = likesRepository.countByPostLiked(post);


        return ToggleLikeResponse.builder()
                .postID(post.getId())
                .liked(true)
                .likeCount(count)
                .message("Post liked")
                .build();


    }
















}
