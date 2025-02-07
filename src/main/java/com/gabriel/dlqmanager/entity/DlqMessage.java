package com.gabriel.dlqmanager.entity;

import com.gabriel.dlqmanager.Enum.ReprocessStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class DlqMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String messagePayload;

    private String reason;

    private String originalQueue;

    private int retryCount;

    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private ReprocessStatus reprocessStatus;

}
