package com.gabriel.dlqmanager.service;

import com.gabriel.dlqmanager.entity.DlqMessage;
import com.gabriel.dlqmanager.repository.DlqMessageRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class DLQService {

    @Autowired
    private DlqMessageRepository dlqMessageRepository;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @RabbitListener(queues = "${rabbitmq.queue.dlq}")
    public void processDlqMessage(Message message){

        Map<String, Object> headers = message.getMessageProperties().getHeaders();

        String reason = null;
        String originalQueue = null;
        int retryCount = 0;

        if (headers.containsKey("x-death")){
            List<Map<String, Object>> xDeath = (List<Map<String, Object>>) headers.get("x-death");
            System.out.println(message);

            if(!xDeath.isEmpty()){
                Map<String, Object> deathDetails = xDeath.getFirst();
                reason = (String) deathDetails.get("reason");
                originalQueue = (String) deathDetails.get("queue");
                retryCount = ((Long) deathDetails.get("count")).intValue();
            }
        }
        DlqMessage dlqMessage = new DlqMessage();
        dlqMessage.setMessagePayload(new String(message.getBody()));
        dlqMessage.setReason(reason != null ? reason : "Unknown");
        dlqMessage.setOriginalQueue(originalQueue != null ? originalQueue : "Unknown");
        dlqMessage.setRetryCount(retryCount);
        dlqMessage.setReprocessed(false);
        dlqMessage.setTimestamp(LocalDateTime.now());

        dlqMessageRepository.save(dlqMessage);
    }

    public String reprocessDlqMessage(Long id){
        DlqMessage dlqMessage = dlqMessageRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        if (dlqMessage.isReprocessed()){
            return "Message already reprocessed!";
        }

        reprocess(dlqMessage, "success");

        return "Message reprocessed!";
    }

    public String reprocessListOfDlqMessages(List<Long> ids){
        List<DlqMessage> dlqMessages = dlqMessageRepository.findAllById(ids);

        for (DlqMessage dlqMessage : dlqMessages){
            if (!dlqMessage.isReprocessed()){
                reprocess(dlqMessage, "success");

            }
        }
        return "All messages in the list reprocessed!";
    }

    public Page<DlqMessage> findAll(String reason, String queue, boolean reprocessed, int page, int size){
        return dlqMessageRepository.findMessagesWithFilters(reason, queue, reprocessed, PageRequest.of(page, size));
    }

    public String reprocessAllMessages(){
        dlqMessageRepository.findAll().forEach(dlqMessage -> {
            if (!dlqMessage.isReprocessed()){
                reprocess(dlqMessage, "success");
            }
        });
        return "All messages reprocessed!";
    }

    public void reprocess(DlqMessage dlqMessage, String message){
        rabbitMQProducer.sendMessage(message);
        dlqMessage.setReprocessed(true);
        dlqMessageRepository.save(dlqMessage);
    }
}
