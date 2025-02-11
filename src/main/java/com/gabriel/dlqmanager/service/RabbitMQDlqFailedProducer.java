package com.gabriel.dlqmanager.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQDlqFailedProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.dlqReprocessingFailed}")
    private String exchange;

    @Value("${rabbitmq.queue.dlqReprocessingFailed}")
    private String bindingKey;

    public void sendToFailedQueue(Long id){
        rabbitTemplate.convertAndSend(exchange, bindingKey, id);
    }
}
