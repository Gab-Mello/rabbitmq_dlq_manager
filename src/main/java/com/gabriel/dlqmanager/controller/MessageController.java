package com.gabriel.dlqmanager.controller;

import com.gabriel.dlqmanager.service.RabbitMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @PostMapping
    public ResponseEntity<String> publishMessage(){
        rabbitMQProducer.sendMessage("fail");
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
