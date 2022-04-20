package org.example;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MqConsumerApplication {

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("files_topic_exchange");
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.nonDurable().withArgument("x-dead-letter-exchange", "deadLetterExchange")
                .withArgument("x-dead-letter-routing-key", "deadLetter").build();
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange topicExchange) {
        return BindingBuilder.bind(queue).to(topicExchange).with("files.images.jpg");
    }

    @Bean
    public RabbitListenerContainerFactory<SimpleMessageListenerContainer> containerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPrefetchCount(10);
        factory.setConcurrentConsumers(5);
        return factory;
    }

    public static void main(String[] args) {
        SpringApplication.run(MqConsumerApplication.class, args);
    }
}
