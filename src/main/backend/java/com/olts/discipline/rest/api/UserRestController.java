package com.olts.discipline.rest.api;

import com.olts.discipline.api.service.*;
import com.olts.discipline.entity.*;
import com.olts.discipline.rest.dto.UserGETDto;
import com.olts.discipline.rest.dto.UserPutDto;
import com.olts.discipline.rest.hateoas.PageableResource;
import com.olts.discipline.rest.hateoas.assembler.PageableResourceAssembler;
import com.olts.discipline.rest.mapper.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.domain.Page;
import org.springframework.data.rest.core.event.AfterSaveEvent;
import org.springframework.data.rest.core.event.BeforeSaveEvent;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * OLTS on 29.08.2017.
 */
@RepositoryRestController
public class UserRestController implements ApplicationEventPublisherAware {
    //todo get normal link
    public static ControllerLinkBuilder linkToUserHabits(Long userId) {
        return ControllerLinkBuilder.linkTo(UserRestController.class).slash(String.format("api/users/%x/habits", userId));
    }
    public static ControllerLinkBuilder linkToUserGroups(Long userId) {
        return ControllerLinkBuilder.linkTo(UserRestController.class).slash(String.format("api/users/%x/groups", userId));
    }
    public static ControllerLinkBuilder linkToAvailableUserChallenges(Long userId) {
        return ControllerLinkBuilder.linkTo(UserRestController.class).slash(String.format("api/users/%x/challenges", userId));
    }
    public static ControllerLinkBuilder linkToUserChallenges(Long userId) {
        return ControllerLinkBuilder.linkTo(UserRestController.class).slash(String.format("api/users/%x/userchallenges", userId));
    }
    private ApplicationEventPublisher publisher;
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
    @Resource
    EntityLinks entityLinks;
    @Resource
    private UserService userService;
    @Resource
    private HabitService habitService;
    @Resource
    private GroupService groupService;
    @Resource
    private ChallengeService challengeService;
    @Resource
    private UserChallengeService userChallengeService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private HabitMapper habitMapper;
    @Resource
    private GroupMapper groupMapper;
    @Resource
    private ChallengeMapper challengeMapper;
    @Resource
    private UserChallengeMapper userChallengeMapper;

    @GetMapping("/users/current")
    private @ResponseBody ResponseEntity<UserGETDto> getCurrent() {
        User current = userService.getCurrent();
        UserGETDto userGETDto = userMapper.pojoToDto(current);
        userGETDto.add(entityLinks.linkForSingleResource(User.class, current.getId()).withSelfRel());
        return ResponseEntity.ok(userGETDto);
    }

    @GetMapping("/users/current/edit")
    private @ResponseBody ResponseEntity<UserPutDto> getCurrentForEdit() {
        User current = userService.getCurrent();
        UserPutDto userPutDto = userMapper.userToUserPutDto(current);
        userPutDto.add(entityLinks.linkForSingleResource(User.class, current.getId()).withSelfRel());
        return ResponseEntity.ok(userPutDto);
    }

    @GetMapping("/users/{userId}/habits")
    private @ResponseBody ResponseEntity<PageableResource> getUserHabits(
            @PathVariable("userId") Long userId,
            @RequestParam(value="completed", defaultValue="false") Boolean completed,
            @RequestParam(value="achieved", defaultValue="false") Boolean achieved,
            @RequestParam(value="page", defaultValue="0") Integer page,
            @RequestParam(value="size", defaultValue="5") Integer size) {
        return new ResponseEntity<>(getUserHabitsResponse(userId, completed, achieved, page, size), HttpStatus.OK);
    }

    @GetMapping("/users/current/habits")
    private @ResponseBody ResponseEntity<PageableResource> getCurrentUserHabits(
            @RequestParam(value="completed", defaultValue="false") Boolean completed,
            @RequestParam(value="achieved", defaultValue="false") Boolean achieved,
            @RequestParam(value="page", defaultValue="0") Integer page,
            @RequestParam(value="size", defaultValue="5") Integer size) {
        User current = userService.getCurrent();
        return new ResponseEntity<>(getUserHabitsResponse(current.getId(), completed, achieved, page, size), HttpStatus.OK);
    }

    // todo move to org.springframework.data.web.PagedResourcesAssembler
    private PageableResource getUserHabitsResponse(Long userId, Boolean completed, Boolean achieved, Integer page, Integer size) {
        String methodPath = linkToUserHabits(userId).toString();
        Page<Habit> habitPage = habitService.getByUserId(userId, achieved, completed, page, size);
        Map<String, String> params = Collections.unmodifiableMap(new HashMap<String, String>(){{put("completed", String.valueOf(completed)); put("achieved", String.valueOf(achieved));}});
        return new PageableResourceAssembler<>(habitMapper, methodPath, params).toResource(habitPage);
    }

    @GetMapping("/users/{userId}/groups")
    private @ResponseBody ResponseEntity<PageableResource> getUserGroups(
            @PathVariable("userId") Long userId,
            @RequestParam(value="page", defaultValue="0") Integer page,
            @RequestParam(value="size", defaultValue="10") Integer size) {
        String path = linkToUserGroups(userId).toString();
        Page<Group> groups = groupService.getByUserId(userId, page, size);
        return new ResponseEntity<>(new PageableResourceAssembler<>(groupMapper, path, null).toResource(groups), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/challenges")
    private @ResponseBody ResponseEntity<PageableResource> getAvailableUserChallenges(
            @PathVariable("userId") Long userId,
            @RequestParam(value="createdBy", defaultValue="false") Boolean createdBy,
            @RequestParam(value="page", defaultValue="0") Integer page,
            @RequestParam(value="size", defaultValue="10") Integer size) {
        return new ResponseEntity<>(getAvailableChallenges(userId, createdBy, page, size), HttpStatus.OK);
    }

    @GetMapping("/users/current/challenges")
    private @ResponseBody ResponseEntity<PageableResource> getCurrentAvailableChallenges(
            @RequestParam(value="createdBy", defaultValue="false") Boolean createdBy,
            @RequestParam(value="page", defaultValue="0") Integer page,
            @RequestParam(value="size", defaultValue="10") Integer size) {
        return new ResponseEntity<>(getAvailableChallenges(userService.getCurrent().getId(), createdBy, page, size), HttpStatus.OK);
    }

    private PageableResource getAvailableChallenges(Long userId, Boolean createdBy, int page, int size) {
        String path = linkToAvailableUserChallenges(userId).toString();
        Page<Challenge> challenges = createdBy ? challengeService.getByCreatedByUserId(userId, page, size) : challengeService.getByUserId(userId, page, size);
        Map<String, String> params = Collections.unmodifiableMap(new HashMap<String, String>(){{put("createdBy", String.valueOf(createdBy));}});
        PageableResourceAssembler<Challenge> assembler = new PageableResourceAssembler<>(challengeMapper, path, params);
        return assembler.toResource(challenges);
    }

    @GetMapping("/users/{userId}/userchallenges")
    private @ResponseBody ResponseEntity<PageableResource> getUserChallenges(
            @PathVariable("userId") Long userId,
            @RequestParam(value="completed", defaultValue="false") Boolean completed,
            @RequestParam(value="page", defaultValue="0") Integer page,
            @RequestParam(value="size", defaultValue="10") Integer size) {
        return new ResponseEntity<>(userChallenges(userId, completed, page, size), HttpStatus.OK);
    }

    @GetMapping("/users/current/userchallenges")
    private @ResponseBody ResponseEntity<PageableResource> getCurrentUserChallenges(
            @RequestParam(value="completed", defaultValue="false") Boolean completed,
            @RequestParam(value="page", defaultValue="0") Integer page,
            @RequestParam(value="size", defaultValue="10") Integer size) {
        return new ResponseEntity<>(userChallenges(userService.getCurrent().getId(), completed, page, size), HttpStatus.OK);
    }

    private PageableResource userChallenges(Long userId, boolean completed, int page, int size) {
        String path = linkToUserChallenges(userId).toString();
        Page<UserChallenge> userChallenges = userChallengeService.getByUserId(userId, completed, page, size);
        Map<String, String> params = Collections.unmodifiableMap(new HashMap<String, String>(){{put("completed", String.valueOf(completed));}});
        return new PageableResourceAssembler<>(userChallengeMapper, path, params).toResource(userChallenges);
    }

    @PutMapping("/users/{userId}")
    private @ResponseBody ResponseEntity<UserGETDto> update(@PathVariable("userId") String userId, @RequestBody org.springframework.hateoas.Resource<User> input) {
        User out = userService.get(Long.parseLong(userId));
        User in = input.getContent();
        publisher.publishEvent(new BeforeSaveEvent(in));
        propagatePut(in, out);
        userService.update(out);
        publisher.publishEvent(new AfterSaveEvent(in));
        return ResponseEntity.ok(userMapper.pojoToDto(in));
    }

    /**
     * PUT pojo propagation
     * */
    private void propagatePut(User in, User out) {
        out.setFirstName(in.getFirstName());
        out.setSecondName(in.getSecondName());
        out.setLastName(in.getLastName());
        out.setEmail(in.getEmail());
        out.setBirthDate(in.getBirthDate());
        out.setCity(in.getCity());
        out.setCountry(in.getCountry());
    }
}