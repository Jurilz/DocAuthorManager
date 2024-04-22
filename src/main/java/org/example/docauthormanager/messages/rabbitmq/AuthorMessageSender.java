package org.example.docauthormanager.messages.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuthorMessageSender {
    private final RabbitTemplate rabbitTemplate;

    public AuthorMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendAuthorMessage(String message) {
        rabbitTemplate.convertAndSend(RabbitMqConfiguration.EXCHANGE_NAME, RabbitMqConfiguration.AUTHOR_KEY,
                message);
    }
}