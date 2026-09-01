package com.example.MindConnect.Service;

import com.example.MindConnect.Entity.MessageEntity;
import com.example.MindConnect.Entity.UserEntity;
import com.example.MindConnect.Enums.AccountStatus;
import com.example.MindConnect.Payload.Request.MessageRequest.EditMessageRequest;
import com.example.MindConnect.Payload.Request.MessageRequest.SendMessageRequest;
import com.example.MindConnect.Payload.Response.MessageResponse.*;
import com.example.MindConnect.Repository.MessageRepository;
import com.example.MindConnect.Repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Data

public class MessageServiceImpl {

    private final UserRepository userRepository;

    private final MessageRepository messageRepository;


    //TODO - UNREAD MESSAGE COUNT, EDIT MESSAGE, DELETE CONVERSATION, SEARCH CONVERSATION

    public SendMessageResponse sendMessage(SendMessageRequest request) {


        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity sender = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        if (sender.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("You cannot send messages at this time");
        }

        UserEntity recipient = userRepository.findById(request.getRecipientID())
                .orElseThrow(() -> new RuntimeException("Message recipient not found"));


        if (recipient.getStatus() != AccountStatus.ACTIVE) {

            throw new RuntimeException("This user cannot receive messages at this time");
        }

        if (request.getContent() == null) {
            throw new RuntimeException("Message cannot be empty");
        }

        if(sender.getId().equals(recipient.getId())){

            throw new RuntimeException("You are not allowed to send messages to yourself");
        }


        MessageEntity message = MessageEntity.builder()
                .sender(sender)
                .recipient(recipient)
                .message(request.getContent())
                .sentAt(LocalDateTime.now())
                .isRead(false)
                .readAt(null)
                .build();

        MessageEntity saved_message = messageRepository.save(message);


        SendMessageResponse response = SendMessageResponse.builder()
                .messageID(saved_message.getId())
                .senderID(saved_message.getSender().getId())
                .recipientID(saved_message.getRecipient().getId())
                .senderName(sender.getFirstName() + " " + sender.getLastName())
                .recipientName(recipient.getFirstName() + " " + recipient.getLastName())
                .content(saved_message.getMessage())
                .sentAt(saved_message.getSentAt())
                .read(saved_message.isRead())
                .readAt(saved_message.getReadAt())
                .messageStatus("Message was sent successfully")
                .build();


        return response;
    }


    public List<GetConversationResponse> getConversationWithUser(UUID userID) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        if (currentUser.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("You cannot send messages at this time");
        }

        UserEntity otherUser = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("Message recipient not found"));

        if (otherUser.getStatus() != AccountStatus.ACTIVE) {

            throw new RuntimeException("This user cannot receive messages at this time");
        }

        List<MessageEntity> conversation = messageRepository.
                findBySenderAndRecipientOrSenderAndRecipient(currentUser, otherUser, otherUser, currentUser);

        conversation.removeIf(message -> {

            boolean currentUserSentMessage = message.getSender().getId().equals(currentUser.getId());
            if (currentUserSentMessage){
                return message.isDeletedBySender();
            }

            return message.isDeletedByRecipient();
        });

        List<GetConversationResponse> stored_conversations = new ArrayList<>();

       conversation.sort(Comparator.comparing(MessageEntity::getSentAt));

            for (MessageEntity convo : conversation) {


                if(convo.getRecipient().getEmail().equals(email) && !convo.isRead()){
                    convo.setRead(true);
                    convo.setReadAt(LocalDateTime.now());
                    messageRepository.save(convo);

                }

                GetConversationResponse response = GetConversationResponse.builder()
                        .messageID(convo.getId())
                        .senderID(convo.getSender().getId())
                        .recipientID(convo.getRecipient().getId())
                        .senderName(convo.getSender().getFirstName() + " " + convo.getSender().getLastName())
                        .recipientName(convo.getRecipient().getFirstName() + " " + convo.getRecipient().getLastName())
                        .content(convo.getMessage())
                        .sentAt(convo.getSentAt())
                        .read(convo.isRead())
                        .readAt(convo.getReadAt())
                        .build();

                stored_conversations.add(response);


            }

            return stored_conversations;


        }

        public List<GetMyChatsResponse> getMyConversations(){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

            UserEntity user = userRepository.findByEmail(email)
                    .orElseThrow(()->new RuntimeException("Authenticated user not found"));

            if(user.getStatus() != AccountStatus.ACTIVE){
                throw new RuntimeException("You cannot see messages at this time");
            }


            List<MessageEntity> messages = messageRepository.findBySenderOrRecipient(user, user);

            messages.removeIf(message -> {

                boolean currentUserSentMessage =
                        message.getSender()
                                .getId()
                                .equals(user.getId());

                if (currentUserSentMessage) {
                    return message.isDeletedBySender();
                }

                return message.isDeletedByRecipient();
            });

            messages.sort(Comparator.comparing(MessageEntity::getSentAt).reversed()); //Descending

            List<GetMyChatsResponse> messages_list = new ArrayList<>();

            Set<UUID> non_duplicate_users = new HashSet<>();

            UserEntity otherUser;

            for(MessageEntity message: messages){

                if(message.getSender().getEmail().equals(email)){

                     otherUser = message.getRecipient();

                }else{

                     otherUser = message.getSender();
                }

                if(non_duplicate_users.contains(otherUser.getId())){
                    continue;
                }


                GetMyChatsResponse response = GetMyChatsResponse.builder()
                        .otherUserID(otherUser.getId())
                        .otherUserName(otherUser.getFirstName() + " " + otherUser.getLastName())
                        .lastMessage(message.getMessage())
                        .lastMessageTime(message.getSentAt())
                        .unreadCount(messageRepository.countBySenderAndRecipientAndIsReadFalseAndDeletedByRecipientFalse(otherUser,user))
                        .build();



                messages_list.add(response);
                non_duplicate_users.add(otherUser.getId());

            }

            return messages_list;

        }


        public EditMessageResponse editMessage(EditMessageRequest request){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Authenticated user not found"));

        if(user.getStatus() != AccountStatus.ACTIVE){

            throw new RuntimeException("You are not allowed to edit messages at this time");
        }

        MessageEntity message = messageRepository.findById(request.getMessageID())
                .orElseThrow(()->new RuntimeException("Message not found"));

        if(message.getSender().getEmail().equals(email)){

            if(request.getNewContent() == null){

                throw new RuntimeException("Content cannot be empty");
            }

            message.setMessage(request.getNewContent());
            message.setUpdatedAt(LocalDateTime.now());
            messageRepository.save(message);

        }else {
            throw new RuntimeException("You are not allowed to edit this message");
        }



            EditMessageResponse response = EditMessageResponse.builder()
                    .messageID(message.getId())
                    .content(message.getMessage())
                    .updatedAt(message.getUpdatedAt())
                    .messageStatus("Message successfully updated")
                    .build();
            return response;



        }



        public DeleteConversationResponse deleteConversation(UUID id){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity currentUser = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Authenticated user not found"));

        if(currentUser.getStatus() != AccountStatus.ACTIVE) {

            throw new RuntimeException("You are not allowed to delete conversations at this time");
        }

         UserEntity otherUser = userRepository.findById(id)
                 .orElseThrow(()->new RuntimeException("Recipient not found"));

         List<MessageEntity> conversation = messageRepository.findBySenderAndRecipientOrSenderAndRecipient(currentUser, otherUser, otherUser, currentUser);

         if(conversation.isEmpty()){
             throw new RuntimeException("Conversation not found");
         }

            long deletedMessageCount = 0;

            for (MessageEntity message : conversation) {

                boolean currentUserSentMessage =
                        message.getSender()
                                .getId()
                                .equals(currentUser.getId());

                if (currentUserSentMessage) {

                    if (!message.isDeletedBySender()) {
                        message.setDeletedBySender(true);
                        deletedMessageCount++;
                    }

                } else {

                    if (!message.isDeletedByRecipient()) {
                        message.setDeletedByRecipient(true);
                        deletedMessageCount++;
                    }
                }
            }

            if (deletedMessageCount == 0) {
                throw new RuntimeException(
                        "Conversation is already deleted"
                );
            }


            messageRepository.saveAll(conversation);

             return DeleteConversationResponse.builder()
                     .otherUserID(otherUser.getId())
                     .otherUserName(otherUser.getFirstName() + " " + otherUser.getLastName())
                     .deletedAt(LocalDateTime.now())
                     .deletedMessageCount(deletedMessageCount)
                     .status("Conversation removed from your messages")
                     .build();

         }



        public Long getUnreadMessages(){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity recipient = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Authenticated user not found"));

        if(recipient.getStatus() != AccountStatus.ACTIVE){

            throw new RuntimeException("You are not allowed to view messages at this time");
        }

        Long count = messageRepository.countByRecipientAndIsReadFalseAndDeletedByRecipientFalse(recipient);

        return count;
        }



        public DeleteMessageResponse deleteMessage(UUID id){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity sender = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Authenticated user not found"));

        MessageEntity message = messageRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Message not found"));


            if(sender.getStatus() != AccountStatus.ACTIVE){
                throw new RuntimeException("You are not allowed to delete messages");
            }



            if(message.getSender().getEmail().equals(email)){

                messageRepository.delete(message);


            }else{


                throw new RuntimeException("You are not authorized to delete this message");
            }

        DeleteMessageResponse response =DeleteMessageResponse.builder()
                .messageID(message.getId())
                .deletedAt(LocalDateTime.now())
                .message("Message has been successfully deleted")
                .build();



        return response;



        }


    }
