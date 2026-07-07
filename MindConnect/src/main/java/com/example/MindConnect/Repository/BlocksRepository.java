package com.example.MindConnect.Repository;

import com.example.MindConnect.Entity.BlockedEntity;
import com.example.MindConnect.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BlocksRepository extends JpaRepository<BlockedEntity,UUID>{

boolean existsByBlockedByAndBlockedUser(UserEntity blockedBy, UserEntity blockedUser);
Optional<BlockedEntity> findByBlockedByAndBlockedUser(UserEntity blockedBy, UserEntity blockedUser);
List<BlockedEntity> findByBlockedBy(UserEntity blockedBy);
}
