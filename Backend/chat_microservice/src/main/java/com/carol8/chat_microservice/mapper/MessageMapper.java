package com.carol8.chat_microservice.mapper;

import com.carol8.chat_microservice.dto.MessageCreateDTO;
import com.carol8.chat_microservice.dto.MessageDTO;
import com.carol8.chat_microservice.dto.MessageListDTO;
import com.carol8.chat_microservice.dto.SentMessageDTO;
import com.carol8.chat_microservice.entity.Message;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageMapper {
    public Message toMessage(SentMessageDTO dto){
        return Message.builder()
                .sender(dto.getSender())
                .receiver(dto.getReceiver())
                .timestamp(LocalDateTime.now())
                .content(dto.getContent())
                .build();
    }
    public MessageDTO toMessageDTO(Message message){
        return MessageDTO.builder()
                .sender(message.getSender())
                .receiver(message.getReceiver())
                .timestamp(message.getTimestamp())
                .content(message.getContent())
                .build();
    }
    public MessageListDTO toMessageListDTO(List<Message> messageList) {
        return MessageListDTO.builder()
                .messageDTOs(messageList.stream()
                        .map(this::toMessageDTO)
                        .collect(Collectors.toList()))
                .build();
    }
}
