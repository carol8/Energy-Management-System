package com.carol8.monitoring_microservice.config;

import com.carol8.monitoring_microservice.service.impl.DeviceMeasurementListenerImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {
    private final Environment environment;
    @Bean
    public Queue createDeviceEventQueue(){
        return new Queue(Objects.requireNonNull(environment.getProperty("rabbitmq.device-event-queue")),true);
    }

    @Bean
    public SimpleMessageListenerContainer listenerContainer(
            ConnectionFactory connectionFactory,
            DeviceMeasurementListenerImpl messageListener
    ){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);

        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(1);
        container.setPrefetchCount(1);

        container.setAcknowledgeMode(AcknowledgeMode.AUTO);

        container.setMessageListener(messageListener);

        System.out.println("Configured container " + container);

        return container;
    }
}
