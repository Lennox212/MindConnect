package com.example.MindConnect.Repository;

import com.example.MindConnect.Entity.LikesEntity;
import com.example.MindConnect.Entity.PostEntity;
import com.example.MindConnect.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LikesRepository extends JpaRepository<LikesEntity, UUID> {


    Boolean existsByLikedByAndPostLiked(UserEntity user, PostEntity post);

    Long countByPostLiked(PostEntity post);

    Optional<LikesEntity> findByLikedByAndPostLiked(UserEntity user, PostEntity post);
}
