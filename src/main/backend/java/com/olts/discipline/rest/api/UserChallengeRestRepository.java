package com.olts.discipline.rest.api;

import com.olts.discipline.api.service.UserChallengeService;
import com.olts.discipline.rest.mapper.UserChallengeMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import javax.annotation.Resource;

/**
 * OLTS on 16.09.2017.
 */

@RepositoryRestController
class UserChallengeRestRepository implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @Resource
    private UserChallengeService challengeService;
    @Resource
    private UserChallengeMapper mapper;

    // todo getChallenges by completedDate
}