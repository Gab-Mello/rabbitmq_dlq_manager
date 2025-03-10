package com.gabriel.dlqmanager.controller;

import com.gabriel.dlqmanager.Enum.ReprocessStatus;
import com.gabriel.dlqmanager.entity.DlqMessage;
import com.gabriel.dlqmanager.service.DLQService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "DLQ Controller", description = "Operations for managing messages in the Dead Letter Queue (DLQ)")
@RestController
@RequestMapping("dlq")
public class DlqController {

    @Autowired
    private DLQService dlqService;

    @Operation(summary = "Get all DLQ messages", description = "Retrieves all messages currently in the DLQ.")
    @GetMapping
    public Page<DlqMessage> listDlqMessages(
            @RequestParam(required = false) String reason,
            @RequestParam(required = false) String queue,
            @RequestParam(required = false) ReprocessStatus reprocessStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return dlqService.findAll(reason, queue, reprocessStatus, page, size);
    }

    @Operation(summary = "Reprocess a specific message", description = "Reprocesses a specific message from the DLQ by resending it to the main queue.")
    @PostMapping("/reprocess/{id}")
    public String reprocessDlqMessage(@PathVariable Long id){
        return dlqService.reprocessDlqMessage(id);
    }

    @Operation(summary = "Reprocess multiple messages", description = "Reprocesses a list of messages from the DLQ.")
    @PostMapping("/reprocess-list")
    public String reprocessListOfDlqMessages(List<Long> ids){
        return dlqService.reprocessListOfDlqMessages(ids);
    }

    @Operation(summary = "Reprocess all messages", description = "Reprocesses all pending messages in the DLQ.")
    @PostMapping("/reprocess-all")
    public String reprocessAllMessages(){
        return dlqService.reprocessAllMessages();
    }

    @Operation(summary = "Delete a DLQ message", description = "Deletes a specific message from the DLQ, removing it permanently.")
    @DeleteMapping("delete/{id}")
    public String deleteMessage(@PathVariable Long id){
        return dlqService.deleteMessage(id);
    }

    @Operation(summary = "Get DLQ metrics", description = "Returns DLQ metrics, including total messages, reprocessing success rate, and failures.")
    @GetMapping("/metrics")
    public Map<String, Object> getDlqMetrics(){
        return dlqService.getDlqMetrics();
    }
}
