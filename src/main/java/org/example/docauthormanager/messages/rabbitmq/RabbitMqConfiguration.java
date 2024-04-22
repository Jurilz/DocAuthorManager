package org.example.docauthormanager.messages.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfiguration {

    public static final String EXCHANGE_NAME = "generalExchange";
    public static final String AUTHOR_QUEUE = "authorQueue";
    public static final String AUTHOR_KEY = "authorEvent";
    public static final String DOCUMENT_QUEUE = "documentQueue";
    public static final String DOCUMENT_KEY = "documentEvent";

    @Bean
    public Queue authorQueue() {
        return new Queue(AUTHOR_QUEUE, false);
    }

    @Bean
    TopicExchange generalExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    Binding authorQueueGeneralExchangebinding(Queue authorQueue, TopicExchange generalExchange) {
        return BindingBuilder.bind(authorQueue).to(generalExchange).with(AUTHOR_KEY);
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }

//    @Bean
//    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        connectionFactory.setHost("localhost");
////        connectionFactory.setUsername("guest");
////        connectionFactory.setPassword(password);
//        return connectionFactory;
//    }

}