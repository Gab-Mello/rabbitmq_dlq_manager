package com.gabriel.dlqmanager.service;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Random;

@Service
public class MainConsumer {

    private final Random random = new Random();


    @RabbitListener(queues = "${rabbitmq.queue.main}")
    public void processMessage(String message, @Header("messageID") Long messageID){
        try{
            System.out.println("Message received: " + message);

            if (random.nextInt(100) < 30 ) {
                System.out.println("Error processing message: " + message);

                throw new RuntimeException("Simulated processing error");
            }

            if (messageID != null){
                return;
            }
            
            System.out.println("Message processed successfully: " + message);

        } catch (Exception e) {
            //deu merda
        }

    }

}
