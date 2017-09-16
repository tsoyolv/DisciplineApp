package com.olts.discipline.rest.api;

import com.olts.discipline.api.service.ChallengeService;
import com.olts.discipline.entity.Challenge;
import com.olts.discipline.entity.User;
import com.olts.discipline.rest.dto.ChallengeDto;
import com.olts.discipline.rest.mapper.ChallengeMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.rest.core.event.AfterSaveEvent;
import org.springframework.data.rest.core.event.BeforeSaveEvent;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

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

    @Resource
    private ChallengeService challengeService;
    @Resource
    private ChallengeMapper mapper;

    @PostMapping("/challenges/")
    private @ResponseBody ResponseEntity<ChallengeDto> create(@RequestBody org.springframework.hateoas.Resource<Challenge> input) {
        Challenge challenge = input.getContent();
        User creator = challenge.getCreatedBy();
        if ((creator.getAllowedChallenges() + 1) > creator.getLevel()) {
            return ResponseEntity.badRequest().build();
        }

        publisher.publishEvent(new BeforeSaveEvent(challenge));
        Challenge created = challengeService.create(challenge);
        publisher.publishEvent(new AfterSaveEvent(challenge));
        return ResponseEntity.ok(mapper.pojoToDto(created));
    }
}
