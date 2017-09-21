package com.olts.discipline.rest.api;

import com.olts.discipline.api.service.ChallengeService;
import com.olts.discipline.api.service.UserChallengeService;
import com.olts.discipline.api.service.UserService;
import com.olts.discipline.entity.Challenge;
import com.olts.discipline.entity.User;
import com.olts.discipline.entity.UserChallenge;
import com.olts.discipline.rest.dto.ChallengeDto;
import com.olts.discipline.rest.hateoas.PageableResource;
import com.olts.discipline.rest.hateoas.assembler.PageableResourceAssembler;
import com.olts.discipline.rest.mapper.ChallengeMapper;
import com.olts.discipline.rest.mapper.UserChallengeMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.domain.Page;
import org.springframework.data.rest.core.event.AfterCreateEvent;
import org.springframework.data.rest.core.event.AfterSaveEvent;
import org.springframework.data.rest.core.event.BeforeSaveEvent;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * OLTS on 15.09.2017.
 */
@RepositoryRestController
public class ChallengeRestController implements ApplicationEventPublisherAware {
    public static ControllerLinkBuilder linkToUserChallenges(Long challengeId) {
        return ControllerLinkBuilder.linkTo(UserRestController.class).slash(String.format("api/challenges/%x/userchallenges", challengeId));
    }
    public static ControllerLinkBuilder linkToChallenge(Long challengeId) {
        return ControllerLinkBuilder.linkTo(UserRestController.class).slash(String.format("challenge/%x", challengeId));
    }
    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @Resource
    private ChallengeService challengeService;
    @Resource
    private UserChallengeService userChallengeService;
    @Resource
    private UserService userService;
    @Resource
    private ChallengeMapper mapper;
    @Resource
    private UserChallengeMapper userChallengeMapper;

    @GetMapping("/challenges/{challengeId}")
    private @ResponseBody ResponseEntity<ChallengeDto> getChallenge(@PathVariable("challengeId") Long challengeId) {
        return ResponseEntity.ok(mapper.pojoToDto(challengeService.get(challengeId)));
    }

    @GetMapping("/challenges/{challengeId}/userchallenges")
    private @ResponseBody ResponseEntity<PageableResource> getUserChallenges(
            @PathVariable("challengeId") Long challengeId,
            @RequestParam(value="completed", defaultValue="false") Boolean completed,
            @RequestParam(value="page", defaultValue="0") Integer page,
            @RequestParam(value="size", defaultValue="10") Integer size) {
        String path = linkToUserChallenges(challengeId).toString();
        Page<UserChallenge> userChallenges = userChallengeService.getByOriginalChallengeId(challengeId, completed, page, size);
        Map<String, String> params = Collections.unmodifiableMap(new HashMap<String, String>(){{put("completed", String.valueOf(completed));}});
        return new ResponseEntity<>(new PageableResourceAssembler<>(userChallengeMapper, path, params).toResource(userChallenges), HttpStatus.OK);
    }

    @PostMapping("/challenges/")
    private @ResponseBody ResponseEntity<ChallengeDto> create(@RequestBody org.springframework.hateoas.Resource<Challenge> input) {
        Challenge challenge = input.getContent();
        User creator = userService.getCurrent();
        if ((creator.getAllowedChallenges() + 1) > creator.getLevel()) {
            return ResponseEntity.badRequest().build();
        }
        Challenge postDtoToChallenge = challenge;
        postDtoToChallenge.setCreatedBy(creator);
        Challenge created = challengeService.create(postDtoToChallenge);
        publisher.publishEvent(new AfterSaveEvent(challenge));
        return ResponseEntity.ok(mapper.pojoToDto(created));
    }

    @PutMapping("/challenges/{challengeId}/accept")
    private @ResponseBody ResponseEntity<ChallengeDto> accept(@PathVariable("challengeId") Long challengeId) {
        UserChallenge accept = userChallengeService.accept(challengeId, userService.getCurrent().getId());
        publisher.publishEvent(new AfterCreateEvent(accept));
        publisher.publishEvent(new AfterSaveEvent(challengeService.get(challengeId)));
        return ResponseEntity.ok(mapper.pojoToDto(challengeService.get(challengeId)/* get again after changes */));
    }

    @PutMapping("/challenges/{challengeId}/vote")
    private @ResponseBody ResponseEntity<ChallengeDto> vote(@PathVariable("challengeId") Long challengeId) {
        Challenge challenge = challengeService.get(challengeId);
        publisher.publishEvent(new BeforeSaveEvent(challenge));
        challenge.setVotes(challenge.getVotes() + 1);
        challenge.addVotedUser(userService.getCurrent());
        challengeService.update(challenge);
        publisher.publishEvent(new AfterSaveEvent(challenge));
        return ResponseEntity.ok(mapper.pojoToDto(challenge));
    }
}
