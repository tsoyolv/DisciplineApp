package org.discipline.api.service;

import com.olts.discipline.entity.Message;
import org.springframework.data.domain.Page;

/**
 * OLTS on 22.09.2017.
 */

public interface MessageService {
    Page<Message> get(Integer page, Integer size);
    Page<Message> getByChallengeId(Long challengeId, Integer page, Integer size);
    Message get(Long id);
    Message send(Long userId, Long challengeId, String message);
    Message update(Message message);
    void delete(Long id);
}
