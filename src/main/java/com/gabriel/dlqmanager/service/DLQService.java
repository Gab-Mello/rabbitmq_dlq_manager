package com.gabriel.dlqmanager.service;

import com.gabriel.dlqmanager.Enum.ReprocessStatus;
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
    private RabbitMQDlqProducer rabbitMQDlqProducer;

    @RabbitListener(queues = "${rabbitmq.queue.dlq}")
    public void processDlqMessage(Message message){
        Map<String, Object> headers = message.getMessageProperties().getHeaders();

        System.out.println("HEADERS: "+ headers.get("messageId"));

        if (headers.get("messageId") != null){
            Long messageId = (Long) headers.get("messageId");
            DlqMessage dlqMessage = dlqMessageRepository.findById(messageId).orElseThrow(EntityNotFoundException::new);
            dlqMessage.setReprocessStatus(ReprocessStatus.FAILED);
            dlqMessageRepository.save(dlqMessage);
        }
        else {
            String reason = null;
            String originalQueue = null;
            int retryCount = 0;

            if (headers.containsKey("x-death")){
                List<Map<String, Object>> xDeath = (List<Map<String, Object>>) headers.get("x-death");

                if(!xDeath.isEmpty()){
                    Map<String, Object> deathDetails = xDeath.getFirst();
                    reason = (String) deathDetails.get("reason");
                    originalQueue = (String) deathDetails.get("queue");
                }
            }
            DlqMessage dlqMessage = new DlqMessage();
            dlqMessage.setMessagePayload(new String(message.getBody()).replace("\"", ""));
            dlqMessage.setReason(reason != null ? reason : "Unknown");
            dlqMessage.setOriginalQueue(originalQueue != null ? originalQueue : "Unknown");
            dlqMessage.setRetryCount(retryCount);
            dlqMessage.setReprocessStatus(ReprocessStatus.PENDING);
            dlqMessage.setTimestamp(LocalDateTime.now());
            dlqMessage.setRetryCount(0);

            dlqMessageRepository.save(dlqMessage);
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.dlqReprocessingSucceeded}")
    public void updateReprocessStatusToSuccess(Message message){
        Long messageId = Long.parseLong(new String(message.getBody()));
        DlqMessage dlqMessage = dlqMessageRepository.findById(messageId).orElseThrow(EntityNotFoundException::new);
        dlqMessage.setReprocessStatus(ReprocessStatus.SUCCESS);
        dlqMessageRepository.save(dlqMessage);
    }

    public String reprocessDlqMessage(Long id){
        DlqMessage dlqMessage = dlqMessageRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        if (dlqMessage.getReprocessStatus() == ReprocessStatus.PENDING || dlqMessage.getReprocessStatus() == ReprocessStatus.FAILED){
            reprocess(dlqMessage);
            return "Message reprocessed!";
        }

        return "Message already reprocessed!";
    }

    public String reprocessListOfDlqMessages(List<Long> ids){
        List<DlqMessage> dlqMessages = dlqMessageRepository.findAllById(ids);

        for (DlqMessage dlqMessage : dlqMessages){
            if (dlqMessage.getReprocessStatus() == ReprocessStatus.PENDING){
                reprocess(dlqMessage);
            }
        }
        return "All messages in the list reprocessed!";
    }

    public String reprocessAllMessages(){
        List<DlqMessage> dlqMessages = dlqMessageRepository.findAll();

        for (DlqMessage dlqMessage : dlqMessages){
            if (dlqMessage.getReprocessStatus() == ReprocessStatus.PENDING){
                reprocess(dlqMessage);
            }
        };
        return "All messages reprocessed!";
    }

    public void reprocess(DlqMessage dlqMessage){
        try{
            rabbitMQDlqProducer.sendDlqMessage(dlqMessage.getMessagePayload(), dlqMessage.getId());
            dlqMessage.setRetryCount(dlqMessage.getRetryCount() + 1);
            dlqMessageRepository.save(dlqMessage);
        }
        catch (Exception e){
            dlqMessage.setReprocessStatus(ReprocessStatus.FAILED);
            dlqMessageRepository.save(dlqMessage);
        }

    }

    public Page<DlqMessage> findAll(String reason, String queue, ReprocessStatus reprocessStatus, int page, int size){
        return dlqMessageRepository.findMessagesWithFilters(reason, queue, reprocessStatus, PageRequest.of(page, size));
    }

    public String deleteMessage(Long id){
        DlqMessage dlqMessage = dlqMessageRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        if (dlqMessage.getReprocessStatus() == ReprocessStatus.SUCCESS){
            dlqMessageRepository.delete(dlqMessage);
            return ("Successfully deleted!");
        }
        else {
            throw new IllegalStateException("Only successfully reprocessed messages can be deleted.");
        }
    }


}
