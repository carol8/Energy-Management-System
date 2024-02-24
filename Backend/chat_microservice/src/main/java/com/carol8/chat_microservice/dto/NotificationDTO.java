package com.carol8.chat_microservice.dto;

import com.carol8.chat_microservice.enums.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private String sender;
    private String receiver;
    private Notification notification;
}
