package com.disciplineapp.api.service;

import com.disciplineapp.entity.Challenge;
import org.springframework.data.domain.Page;

/**
 * OLTS on 15.09.2017.
 */
public interface ChallengeService {
    Page<Challenge> get(Boolean completed, Integer page, Integer size);
    Page<Challenge> getByUserId(Long userId, Integer page, Integer size);
    Page<Challenge> getByCreatedByUserId(Long userId, Integer page, Integer size);
    Challenge get(Long id);
    Challenge create(Challenge challenge);
    Challenge update(Challenge challenge);
    void delete(Long id);
}
