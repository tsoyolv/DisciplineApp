package com.olts.discipline.rest.api;

import com.olts.discipline.entity.Challenge;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * OLTS on 15.09.2017.
 */
@RepositoryRestController
class ChallengeRestRepository implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @PostMapping("/challenges/")
    private @ResponseBody
    ResponseEntity<Challenge> update(@RequestBody org.springframework.hateoas.Resource<Challenge> input) {
        return null; //todo
    }
}
