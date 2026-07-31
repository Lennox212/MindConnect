package com.example.MindConnect.Repository;

import com.example.MindConnect.Entity.PostEntity;
import com.example.MindConnect.Entity.UserEntity;
import com.example.MindConnect.Enums.PostVisibility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<PostEntity, UUID> {


    List<PostEntity> findByAuthor(UserEntity author);
    List<PostEntity> findByVisibilityOrderByPostedAtDesc(PostVisibility visibility);
}
