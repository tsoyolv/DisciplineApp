package com.olts.discipline.rest.api;

import com.olts.discipline.api.service.HabitService;
import com.olts.discipline.api.service.UserService;
import com.olts.discipline.entity.Habit;
import com.olts.discipline.entity.User;
import com.olts.discipline.rest.hateoas.assembler.PageableResourceAssembler;
import com.olts.discipline.rest.hateoas.PageableResource;
import com.olts.discipline.rest.dto.UserGETDto;
import com.olts.discipline.rest.dto.UserPutDto;
import com.olts.discipline.rest.mapper.HabitMapper;
import com.olts.discipline.rest.mapper.UserMapper;
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

/**
 * OLTS on 29.08.2017.
 */
@RepositoryRestController
class UserRestController implements ApplicationEventPublisherAware {
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
    private UserMapper userMapper;
    @Resource
    private HabitMapper habitMapper;

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
    private @ResponseBody ResponseEntity<PageableResource> getHabits(
            @PathVariable("userId") Long userId,
            @RequestParam(value="completed", defaultValue="false") Boolean completed,
            @RequestParam(value="achieved", defaultValue="false") Boolean achieved,
            @RequestParam(value="page", defaultValue="0") Integer page,
            @RequestParam(value="size", defaultValue="5") Integer size) {
        //todo get normal link
        String methodPath = ControllerLinkBuilder.linkTo(UserRestController.class).slash(String.format("api/users/%x/habits", userId)).toString();
        Page<Habit> habitPage = habitService.getByUserId(userId, achieved, completed, page, size);
        return new ResponseEntity<>(new PageableResourceAssembler<>(habitMapper, methodPath).toResource(habitPage), HttpStatus.OK);
    }

    /*@GetMapping("/users/habits") // todo
    private @ResponseBody ResponseEntity<PagedResources<Habit>> getHabits(
            Pageable pageable,
            PagedResourcesAssembler assembler
            ) {

        Page<Habit> habits = habitService.get(false, false, pageable);
        return new ResponseEntity<>(assembler.toResource(habits), HttpStatus.OK);
    }*/

    /**
     * merge entities
     * */
    @PutMapping("/users/{userId}")
    private @ResponseBody ResponseEntity<?> update(@PathVariable("userId") String userId, @RequestBody org.springframework.hateoas.Resource<User> input) {
        User out = userService.get(Long.parseLong(userId));
        User in = input.getContent();
        publisher.publishEvent(new BeforeSaveEvent(in));
        propagateUserForPut(in, out);
        userService.update(out);
        publisher.publishEvent(new AfterSaveEvent(in));
        return ResponseEntity.ok(in);
    }

    private void propagateUserForPut(User in, User out) {
        out.setFirstName(in.getFirstName());
        out.setSecondName(in.getSecondName());
        out.setLastName(in.getLastName());
        out.setEmail(in.getEmail());
        out.setIsHidden(in.getIsHidden());
        out.setBirthDate(in.getBirthDate());
        out.setCity(in.getCity());
        out.setCountry(in.getCountry());
    }
}