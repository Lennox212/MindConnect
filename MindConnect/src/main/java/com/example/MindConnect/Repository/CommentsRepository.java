package com.example.MindConnect.Repository;

import com.example.MindConnect.Entity.CommentEntity;
import com.example.MindConnect.Entity.PostEntity;
import com.example.MindConnect.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentsRepository extends JpaRepository<CommentEntity, UUID> {

    List<CommentEntity> findByPost(PostEntity post);
    List<CommentEntity>findByCommentedBy(UserEntity user);
}
