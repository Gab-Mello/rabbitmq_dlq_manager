package com.gabriel.dlqmanager.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQDlqProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.main}")
    private String exchange;

    @Value("${rabbitmq.queue.main}")
    private String routingKey;

    public void sendDlqMessage(String message, Long messageId){
        System.out.println("DLQ Producer message id: " + messageId);
        rabbitTemplate.convertAndSend(exchange, routingKey, message, msg -> {
            msg.getMessageProperties().setHeader("messageId", messageId);
            return msg;
        });
    }
}
