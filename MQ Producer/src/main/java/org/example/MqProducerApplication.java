package org.example;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MqProducerApplication {

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("files_topic_exchange");
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable("deadLetterQueue").build();
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange("deadLetterQueue");
    }

    @Bean
    public Binding DlqBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with("deadLetterQueue");
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }

    public static void main(String[] args) {
        SpringApplication.run(MqProducerApplication.class, args);
    }
}
