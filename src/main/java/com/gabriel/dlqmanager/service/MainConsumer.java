package com.gabriel.dlqmanager.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MainConsumer {

    @RabbitListener(queues = "${rabbitmq.queue.main}")
    public void processMessage(String message){
        System.out.println("Message received: " + message);

        if (message.contains("fail")) {
            System.out.println("error: " + message);
            throw new RuntimeException("Erro no processamento da mensagem!");
        }
    }

}
