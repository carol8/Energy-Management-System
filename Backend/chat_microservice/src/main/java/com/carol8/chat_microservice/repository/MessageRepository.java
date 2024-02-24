package com.carol8.chat_microservice.repository;

import com.carol8.chat_microservice.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findAllBySenderOrReceiverOrderByTimestamp(String sender, String receiver);
}
