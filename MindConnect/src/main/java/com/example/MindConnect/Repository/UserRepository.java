package com.example.MindConnect.Repository;

import com.example.MindConnect.Entity.MessageEntity;
import com.example.MindConnect.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

   Optional<UserEntity> findByEmail(String email);
   boolean existsByEmail(String email);
    Optional<UserEntity>findById(UUID id);
}
