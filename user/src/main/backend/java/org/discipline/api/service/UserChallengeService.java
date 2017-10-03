package org.discipline.api.service;

import org.discipline.entity.UserChallenge;
import org.springframework.data.domain.Page;

/**
 * OLTS on 16.09.2017.
 */

public interface UserChallengeService {
    Page<UserChallenge> get(Integer page, Integer size);
    Page<UserChallenge> getByUserId(Long userId, Boolean completed, Integer page, Integer size);
    Page<UserChallenge> getByOriginalChallengeId(Long originalChallengeId, Boolean completed, Integer page, Integer size);
    UserChallenge get(Long id);
    UserChallenge create(UserChallenge challenge);
    UserChallenge update(UserChallenge challenge);
    UserChallenge complete(Long challengeId);
    void delete(Long id);
    UserChallenge accept(Long originalChallengeId, Long userId);
}