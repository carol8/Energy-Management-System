package com.carol8.security_microservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;

@Configuration
@Controller
@RequiredArgsConstructor
@Log
public class WebSocketController {
    @SneakyThrows
    @MessageMapping("/test")
    @SendTo("/topic/test")
    public String handleGraphDataRequest(@Payload String dto){
        log.info(dto);
        Thread.sleep(1000);
        return "Pong";
    }
}
