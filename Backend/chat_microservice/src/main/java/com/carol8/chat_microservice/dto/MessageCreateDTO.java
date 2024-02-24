package com.carol8.chat_microservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageCreateDTO {
    private String sender;
    private String receiver;
    private LocalDateTime timestamp;
    private String content;
}
