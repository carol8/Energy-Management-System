package com.carol8.chat_microservice.service;

import com.carol8.chat_microservice.dto.MessageDTO;
import com.carol8.chat_microservice.dto.NotificationDTO;
import com.carol8.chat_microservice.enums.Notification;

public interface WebSocketService {
    void sendMessageToUser(String user, MessageDTO message);
    void sendNotificationToUser(String user, NotificationDTO notification);
}
