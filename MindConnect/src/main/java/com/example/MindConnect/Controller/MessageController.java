package com.example.MindConnect.Controller;

import com.example.MindConnect.Payload.Request.MessageRequest.EditMessageRequest;
import com.example.MindConnect.Payload.Request.MessageRequest.SendMessageRequest;
import com.example.MindConnect.Payload.Response.MessageResponse.*;
import com.example.MindConnect.Service.MessageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")


public class MessageController {

    private final MessageServiceImpl messageService;


    @PostMapping("/send-message")
    public SendMessageResponse sendMessage(@RequestBody SendMessageRequest request){

        return messageService.sendMessage(request);

    }

    @GetMapping("/get-conversation/{id}")
    public List<GetConversationResponse> getConversationWithUser(@PathVariable UUID id){

        return messageService.getConversationWithUser(id);
    }

    @GetMapping("/get-my-dms")
    public List<GetMyChatsResponse> getMyConversations(){
        return messageService.getMyConversations();
    }

    @GetMapping("/unread-count")
    public Long getUnreadMessages(){

        return messageService.getUnreadMessages();
    }

    @PutMapping("/edit-message")
    public EditMessageResponse editMessage(@RequestBody EditMessageRequest request){

        return messageService.editMessage(request);

    }

    @DeleteMapping("/delete-conversation/{id}")
    public DeleteConversationResponse deleteConversation(@PathVariable UUID id){

        return messageService.deleteConversation(id);
    }

    @DeleteMapping("/delete-message/{id}")
    public DeleteMessageResponse deleteMessage(@PathVariable UUID id){
        return messageService.deleteMessage(id);
    }








}
