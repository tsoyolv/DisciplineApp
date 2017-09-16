package com.olts.discipline.api.service;

import com.olts.discipline.entity.UserChallenge;
import org.springframework.data.domain.Page;

/**
 * OLTS on 16.09.2017.
 */

public interface UserChallengeService {
    Page<UserChallenge> get(Integer page, Integer size);
    Page<UserChallenge> get(Boolean completed, Integer page, Integer size);
    UserChallenge get(Long id);
    UserChallenge create(UserChallenge challenge);
    UserChallenge update(UserChallenge challenge);
    void delete(Long id);
}