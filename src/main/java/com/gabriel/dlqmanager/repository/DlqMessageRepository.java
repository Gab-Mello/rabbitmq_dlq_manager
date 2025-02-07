package com.gabriel.dlqmanager.repository;

import com.gabriel.dlqmanager.Enum.ReprocessStatus;
import com.gabriel.dlqmanager.entity.DlqMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DlqMessageRepository extends JpaRepository<DlqMessage, Long> {

    @Query("SELECT m FROM DlqMessage m " +
            "WHERE (:reason IS NULL OR m.reason = :reason) " +
            "AND (:queue IS NULL OR m.originalQueue = :queue) " +
            "AND (:reprocessStatus IS NULL OR m.reprocessStatus = :reprocessStatus)" +
            "ORDER BY m.id ASC")
    Page<DlqMessage> findMessagesWithFilters(
            @Param("reason") String reason,
            @Param("queue") String queue,
            @Param("reprocessStatus") ReprocessStatus reprocessStatus,
            Pageable pageable
    );
}
