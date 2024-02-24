package com.carol8.chat_microservice.service;

import com.carol8.chat_microservice.dto.MessageCreateDTO;
import com.carol8.chat_microservice.dto.MessageDTO;
import com.carol8.chat_microservice.dto.MessageListDTO;
import com.carol8.chat_microservice.dto.SentMessageDTO;

public interface MessageService {
    MessageListDTO getMessageWithSenderOrReceiver(String senderOrReceiver);
    MessageDTO createMessage(SentMessageDTO dto);
}
