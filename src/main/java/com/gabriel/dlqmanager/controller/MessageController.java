package com.gabriel.dlqmanager.controller;

import com.gabriel.dlqmanager.service.RabbitMQProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Message Controller", description = "Operations for sending messages to the main queue")
@RestController
@RequestMapping("message")
public class MessageController {

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @Operation(summary = "Send a new message", description = "Sends a new message to the main queue.")
    @PostMapping
    public ResponseEntity<String> publishMessage(){
        rabbitMQProducer.sendMessage("fail");
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
