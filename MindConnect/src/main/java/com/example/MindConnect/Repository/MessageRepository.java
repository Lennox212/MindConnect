package com.example.MindConnect.Repository;

import com.example.MindConnect.Entity.MessageEntity;
import com.example.MindConnect.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {

Optional<MessageEntity> findById(UUID id);
List<MessageEntity> findBySenderAndRecipientOrSenderAndRecipient(UserEntity sender, UserEntity recipient, UserEntity receiver, UserEntity messenger);
List<MessageEntity> findBySenderOrRecipient(UserEntity user, UserEntity recipient);
Long countBySenderAndRecipientAndIsReadFalse(UserEntity user, UserEntity recipient);
long countByRecipientAndIsReadFalse(UserEntity recipient);


}
