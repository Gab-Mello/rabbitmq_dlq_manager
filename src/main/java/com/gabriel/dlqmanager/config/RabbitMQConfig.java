package com.gabriel.dlqmanager.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.main}")
    private String mainQueue;

    @Value("${rabbitmq.queue.dlq}")
    private String dlqQueue;

    @Value("${rabbitmq.exchange.main}")
    private String mainExchange;

    @Value("${rabbitmq.exchange.dlq}")
    private String dlqExchange;


    @Bean
    public Queue mainQueue(){
        return QueueBuilder.durable(mainQueue)
                .deadLetterExchange(dlqExchange)
                .deadLetterRoutingKey(dlqQueue)
                .ttl(10000)
                .build();
    }

    @Bean
    public Queue dlqQueue(){
        return QueueBuilder.durable(dlqQueue).build();
    }

    @Bean
    public DirectExchange mainExchange(){
        return ExchangeBuilder.directExchange(mainExchange).build();
    }

    @Bean
    public DirectExchange dlqExchange(){return ExchangeBuilder.directExchange(dlqExchange).build();}

    @Bean
    public Binding mainQueueBinding(){
        return BindingBuilder.bind(mainQueue()).to(mainExchange()).with(mainQueue);
    }

    @Bean
    public Binding dlqQueueBinding(){
        return BindingBuilder.bind(dlqQueue()).to(dlqExchange()).with(dlqQueue);
    }

    @Bean
    public RabbitAdmin createRabbitAdmin(ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> startAdmin(RabbitAdmin rabbitAdmin){
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

}
