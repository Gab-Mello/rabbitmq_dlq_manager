package com.gabriel.dlqmanager.repository;

import com.gabriel.dlqmanager.entity.DlqMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DlqMessageRepository extends JpaRepository<DlqMessage, Long> {
}
