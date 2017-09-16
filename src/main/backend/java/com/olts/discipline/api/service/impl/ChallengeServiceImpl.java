package com.olts.discipline.api.service.impl;

import com.olts.discipline.api.repository.ChallengeRepository;
import com.olts.discipline.api.service.ChallengeService;
import com.olts.discipline.entity.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * OLTS on 15.09.2017.
 */
@Service
public class ChallengeServiceImpl implements ChallengeService {

    @Resource
    private ChallengeRepository repository;

    @Override
    public Page<Challenge> get(Boolean completed, Integer page, Integer size) {
        return repository.findByCompleted(completed, new PageRequest(page, size,  Sort.Direction.DESC, "createdWhen"));
    }

    @Override
    public Challenge get(Long id) {
        return repository.findOne(id);
    }

    @Override
    public Challenge create(Challenge challenge) {
        return repository.save(challenge);
    }

    @Override
    public Challenge update(Challenge challenge) {
        // propagate todo
        return null;
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }
}
