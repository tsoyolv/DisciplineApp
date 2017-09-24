package com.olts.discipline.api.repository;

import com.olts.discipline.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * OLTS on 22.09.2017.
 */
@RepositoryRestResource(path = "messages")
public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findByMessageChallengeIdOrderByWasSentDesc(Long messageChallengeId, Pageable pageable);
}
