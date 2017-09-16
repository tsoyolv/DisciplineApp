package com.olts.discipline.api.service.impl;

import com.olts.discipline.api.repository.UserChallengeReposiory;
import com.olts.discipline.api.service.UserChallengeService;
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

    @Override
    public Page<UserChallenge> get(Integer page, Integer size) {
        return reposiory.findAll(new PageRequest(page, size, Sort.Direction.DESC, "completedDate", "acceptedDate"));
    }

    @Override
    public Page<UserChallenge> get(Boolean completed, Integer page, Integer size) {
        if (completed) {
            return reposiory.findCompleted(new PageRequest(page, size, Sort.Direction.DESC, "completedDate"));
        } else {
            return reposiory.findNonCompleted(new PageRequest(page, size, Sort.Direction.DESC, "acceptedDate"));
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
}
