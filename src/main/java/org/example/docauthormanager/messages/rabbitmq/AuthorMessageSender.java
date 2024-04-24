package org.example.docauthormanager.messages.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.docauthormanager.author.events.AuthorEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuthorMessageSender {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public AuthorMessageSender(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendAuthorMessage(AuthorEvent eventMessage) {
        String stringMessage;
        try {
            stringMessage = objectMapper.writeValueAsString(eventMessage);
        } catch (JsonProcessingException e) {
            stringMessage = eventMessage.toString();
        }
        rabbitTemplate.convertAndSend(RabbitMqConfiguration.EXCHANGE_NAME, RabbitMqConfiguration.AUTHOR_KEY,
                stringMessage);
    }
}