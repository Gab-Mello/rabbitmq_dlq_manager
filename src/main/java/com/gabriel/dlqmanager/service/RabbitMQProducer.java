package com.gabriel.dlqmanager.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.main}")
    private String exchange;

    @Value("${rabbitmq.queue.main}")
    private String routingKey;

    public void sendMessage(String message){
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
