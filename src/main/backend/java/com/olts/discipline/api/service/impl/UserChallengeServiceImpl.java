package com.olts.discipline.api.service.impl;

import com.olts.discipline.api.repository.UserChallengeReposiory;
import com.olts.discipline.api.service.ChallengeService;
import com.olts.discipline.api.service.UserChallengeService;
import com.olts.discipline.api.service.UserService;
import com.olts.discipline.entity.Challenge;
import com.olts.discipline.entity.User;
import com.olts.discipline.entity.UserChallenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * OLTS on 16.09.2017.
 */
@Service
public class UserChallengeServiceImpl implements UserChallengeService {
    @Resource
    private UserChallengeReposiory reposiory;
    @Resource
    private ChallengeService challengeService;
    @Resource
    private UserService userService;

    @Override
    public Page<UserChallenge> get(Integer page, Integer size) {
        return reposiory.findAll(new PageRequest(page, size, Sort.Direction.DESC, "completedDate", "acceptedDate"));
    }

    @Override
    public Page<UserChallenge> getByUserId(Long userId, Boolean completed, Integer page, Integer size) {
        if (completed) {
            return reposiory.findByUserCompleted(userId, new PageRequest(page, size, Sort.Direction.DESC, "completedDate"));
        } else {
            return reposiory.findByUserNonCompleted(userId, new PageRequest(page, size, Sort.Direction.DESC, "acceptedDate"));
        }
    }

    @Override
    public Page<UserChallenge> getByOriginalChallengeId(Long originalChallengeId, Boolean completed, Integer page, Integer size) {
        if (completed) {
            return reposiory.findByOriginalChallengeCompleted(originalChallengeId, new PageRequest(page, size, Sort.Direction.DESC, "completedDate"));
        } else {
            return reposiory.findByOriginalChallengeNonCompleted(originalChallengeId, new PageRequest(page, size, Sort.Direction.DESC, "acceptedDate"));
        }
    }

    @Override
    public UserChallenge get(Long id) {
        return reposiory.findOne(id);
    }

    @Override
    public UserChallenge create(UserChallenge challenge) {
        return reposiory.save(challenge);
    }

    @Override
    public UserChallenge update(UserChallenge challenge) {
        return reposiory.save(challenge);
    }

    @Override
    public void delete(Long id) {
        reposiory.delete(id);
    }

    @Override
    public UserChallenge accept(Long originalChallengeId, Long userId) {
        Challenge challenge = challengeService.get(originalChallengeId);
        if (challenge == null) {throw new IllegalArgumentException("original challenge doesn't exist");}
        User user = userService.get(userId);
        challenge.setAcceptedCount(challenge.getAcceptedCount() + 1);
        challenge.addAcceptedUser(user);
        challengeService.update(challenge);

        UserChallenge userChallenge = new UserChallenge();
        userChallenge.setOriginalChallenge(challenge);
        userChallenge.setChallengeUser(user);
        return create(userChallenge);
    }
}
