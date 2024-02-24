package com.carol8.chat_microservice.service.impl;

import com.carol8.chat_microservice.dto.MessageCreateDTO;
import com.carol8.chat_microservice.dto.MessageDTO;
import com.carol8.chat_microservice.dto.MessageListDTO;
import com.carol8.chat_microservice.dto.SentMessageDTO;
import com.carol8.chat_microservice.entity.Message;
import com.carol8.chat_microservice.mapper.MessageMapper;
import com.carol8.chat_microservice.repository.MessageRepository;
import com.carol8.chat_microservice.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    @Override
    public MessageListDTO getMessageWithSenderOrReceiver(String senderOrReceiver) {
        return messageMapper.toMessageListDTO(messageRepository.findAllBySenderOrReceiverOrderByTimestamp(senderOrReceiver, senderOrReceiver));
    }

    @Override
    public MessageDTO createMessage(SentMessageDTO dto) {
        Message message = messageMapper.toMessage(dto);
        messageRepository.save(message);
        return messageMapper.toMessageDTO(message);
    }
}
