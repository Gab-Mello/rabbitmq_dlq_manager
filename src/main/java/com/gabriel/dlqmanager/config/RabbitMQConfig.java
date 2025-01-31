package com.gabriel.dlqmanager.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.main}")
    private String mainQueue;

    @Value("${rabbitmq.queue.dlq}")
    private String dlqQueue;

    @Value("${rabbitmq.exchange.main}")
    private String exchange;

    @Bean
    public Queue mainQueue(){
        return QueueBuilder.durable(mainQueue)
                .deadLetterExchange("")
                .deadLetterRoutingKey(dlqQueue)
                .ttl(5000)
                .build();
    }

    @Bean
    public Queue dlqQueue(){
        return QueueBuilder.durable(dlqQueue).build();
    }

    @Bean
    public DirectExchange mainExchange(){
        return ExchangeBuilder.directExchange(exchange).build();
    }

    @Bean
    public Binding mainQueueBinding(){
        return BindingBuilder.bind(mainQueue()).to(mainExchange()).with(mainQueue);
    }

    @Bean
    public Binding dlqQueueBinding(){
        return BindingBuilder.bind(dlqQueue()).to(mainExchange()).with(dlqQueue);
    }

}
