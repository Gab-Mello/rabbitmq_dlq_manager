package com.gabriel.dlqmanager.controller;

import com.gabriel.dlqmanager.entity.DlqMessage;
import com.gabriel.dlqmanager.service.DLQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("dlq")
public class DlqController {

    @Autowired
    private DLQService dlqService;

    @GetMapping
    public Page<DlqMessage> listDlqMessages(
            @RequestParam(required = false) String reason,
            @RequestParam(required = false) String queue,
            @RequestParam(required = false) boolean reprocessed,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return dlqService.findAll(reason, queue, reprocessed, page, size);
    }

    @PostMapping("/reprocess/{id}")
    public String reprocessDlqMessage(@PathVariable Long id){
        System.out.println(id);
        return dlqService.reprocessDlqMessage(id);
    }
}
