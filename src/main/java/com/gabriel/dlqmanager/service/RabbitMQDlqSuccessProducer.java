package com.gabriel.dlqmanager.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQDlqSuccessProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.dlqReprocessingSucceeded}")
    private String exchange;

    @Value("${rabbitmq.queue.dlqReprocessingSucceeded}")
    private String bindingKey;

    public void sendToSuccessQueue(Long id){
        rabbitTemplate.convertAndSend(exchange, bindingKey, id);
    }
}
