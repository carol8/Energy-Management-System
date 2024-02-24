package com.carol8.chat_microservice.controller;

import com.carol8.chat_microservice.dto.*;
import com.carol8.chat_microservice.enums.Notification;
import com.carol8.chat_microservice.service.MessageService;
import com.carol8.chat_microservice.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Configuration
@Controller
@RequiredArgsConstructor
@Log
public class WebSocketController {
    private final MessageService messageService;
    private final WebSocketService webSocketService;
    @MessageMapping("/messages")
    @SendTo("/chat/messages")
    public MessageListDTO handleFetchMessages(@Payload MessageRequestDTO dto){
        return messageService.getMessageWithSenderOrReceiver(dto.getSenderOrReceiver());
    }

    @MessageMapping("/adminBroadcastMessages")
    @SendTo("/chat/adminBroadcastMessages")
    public MessageListDTO handleAdminBroadcasts(@Payload MessageRequestDTO dto){
        return messageService.getMessageWithSenderOrReceiver(dto.getSenderOrReceiver());
    }

    @MessageMapping("/send")
    public void handleSentMessage(@Payload SentMessageDTO dto){
        MessageDTO messageDTO = messageService.createMessage(dto);
        webSocketService.sendMessageToUser(messageDTO.getSender(), messageDTO);
        if(!messageDTO.getSender().equals(messageDTO.getReceiver())) {
            webSocketService.sendMessageToUser(messageDTO.getReceiver(), messageDTO);
        }
    }

    @MessageMapping("/sendAdminBroadcast")
    public void handleSentAdminBroadcast(@Payload SentMessageDTO dto){
        MessageDTO messageDTO = messageService.createMessage(dto);
        webSocketService.sendMessageToUser("adminBroadcast", messageDTO);
    }

    @MessageMapping("/seen")
    public void handleSeen(@Payload NotificationDTO dto){
        webSocketService.sendNotificationToUser(dto.getReceiver(), dto);
    }
}
