package com.carol8.chat_microservice.service.impl;

import com.carol8.chat_microservice.dto.MessageDTO;
import com.carol8.chat_microservice.dto.NotificationDTO;
import com.carol8.chat_microservice.enums.Notification;
import com.carol8.chat_microservice.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketServiceImpl implements WebSocketService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void sendMessageToUser(String user, MessageDTO message) {
        simpMessagingTemplate.convertAndSend("/chat/" + user, message);
    }

    @Override
    public void sendNotificationToUser(String user, NotificationDTO notification) {
        simpMessagingTemplate.convertAndSend("/chat/" + user + "/notifications", notification);
    }
}
