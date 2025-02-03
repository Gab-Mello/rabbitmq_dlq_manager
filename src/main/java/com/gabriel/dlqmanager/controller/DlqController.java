package com.gabriel.dlqmanager.controller;

import com.gabriel.dlqmanager.entity.DlqMessage;
import com.gabriel.dlqmanager.service.DLQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("dlq")
public class DlqController {

    @Autowired
    private DLQService dlqService;

    @GetMapping
    public List<DlqMessage> listDlqMessages(){
        return dlqService.findAll();
    }

    @PostMapping("/reprocess/{id}")
    public String reprocessDlqMessage(@PathVariable Long id){
        System.out.println(id);
        return dlqService.reprocessDlqMessage(id);
    }
}
