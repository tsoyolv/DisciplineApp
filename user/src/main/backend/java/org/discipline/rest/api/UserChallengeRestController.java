package org.discipline.rest.api;

import org.discipline.api.service.UserChallengeService;
import org.discipline.api.service.UserService;
import org.discipline.entity.UserChallenge;
import org.discipline.rest.dto.UserChallengeDto;
import org.discipline.rest.mapper.UserChallengeMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.rest.core.event.AfterSaveEvent;
import org.springframework.data.rest.core.event.BeforeSaveEvent;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * OLTS on 16.09.2017.
 */

@RepositoryRestController
class UserChallengeRestController implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @Resource
    private UserChallengeService challengeService;
    @Resource
    private UserService userService;
    @Resource
    private UserChallengeMapper mapper;

    @PutMapping("/user_challenges/{challengeId}/complete")
    private @ResponseBody
    ResponseEntity<UserChallengeDto> complete(@PathVariable("challengeId") Long challengeId) {
        UserChallenge completed = challengeService.complete(challengeId);
        publisher.publishEvent(new AfterSaveEvent(completed));
        publisher.publishEvent(new AfterSaveEvent(completed.getOriginalChallenge()));
        return ResponseEntity.ok(mapper.pojoToDto(completed));
    }

    @PutMapping("/user_challenges/{challengeId}/vote")
    private @ResponseBody ResponseEntity<UserChallengeDto> vote(@PathVariable("challengeId") Long challengeId) {
        UserChallenge challenge = challengeService.get(challengeId);
        if (challenge.getVotedUsers().contains(userService.getCurrent())) {
            return ResponseEntity.badRequest().build();
        }
        publisher.publishEvent(new BeforeSaveEvent(challenge));
        challenge.setVotes(challenge.getVotes() + 1);
        challenge.addVotedUser(userService.getCurrent());
        challengeService.update(challenge);
        publisher.publishEvent(new AfterSaveEvent(challenge));
        return ResponseEntity.ok(mapper.pojoToDto(challenge));
    }


    // todo getChallenges by completedDate
}