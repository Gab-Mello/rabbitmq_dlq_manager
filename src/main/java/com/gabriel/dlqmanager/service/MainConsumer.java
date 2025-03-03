package com.gabriel.dlqmanager.service;

import com.rabbitmq.client.Channel;
import jakarta.annotation.Nullable;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Random;

@Service
public class MainConsumer {

    private final Random random = new Random();

    @Autowired
    private RabbitMQDlqFailedProducer rabbitMQDlqFailedProducer;

    @Autowired
    private RabbitMQDlqSuccessProducer rabbitMQDlqSuccessProducer;

    @RabbitListener(queues = "${rabbitmq.queue.main}" )
    public void processMessage(String message, @Nullable @Header("messageId") Long messageId){

        if (random.nextInt(100) < 50) {
            System.out.println("error: " + message);
            throw new RuntimeException("Erro no processamento da mensagem!");
        }

        if (messageId != null){
            //Send a message to dlq succeed queue
            System.out.println("Mandou para os reprocessamentos que deram bom!");
            rabbitMQDlqSuccessProducer.sendToSuccessQueue(messageId);
        }

    }}


