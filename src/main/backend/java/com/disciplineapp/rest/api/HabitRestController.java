package com.disciplineapp.rest.api;

import com.disciplineapp.api.service.HabitHistoryService;
import com.disciplineapp.api.service.HabitService;
import com.disciplineapp.entity.habit.HabitHistory;
import com.disciplineapp.rest.dto.HabitDto;
import com.disciplineapp.rest.hateoas.PageableResource;
import com.disciplineapp.rest.hateoas.assembler.PageableResourceAssembler;
import com.disciplineapp.rest.mapper.HabitHistoryMapper;
import com.disciplineapp.rest.mapper.HabitMapper;
import com.disciplineapp.api.service.UserService;
import com.disciplineapp.entity.habit.Habit;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.domain.Page;
import org.springframework.data.rest.core.event.AfterSaveEvent;
import org.springframework.data.rest.core.event.BeforeSaveEvent;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * OLTS on 28.05.2017.
 */
@RepositoryRestController
public class HabitRestController implements ApplicationEventPublisherAware {
    public static ControllerLinkBuilder linkToHabitHistories(@PathVariable("habitId") Long habitId) {
        return ControllerLinkBuilder.linkTo(UserRestController.class).slash(String.format("api/habits/%x/histories", habitId));
    }
    @Resource
    private UserService userService;

    @Resource
    private HabitService habitService;
    @Resource
    private HabitHistoryService habitHistoryService;
    @Resource
    private HabitMapper habitMapper;
    @Resource
    private HabitHistoryMapper historyMapper;

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    /**
     * merge entities
     * */
    @PutMapping("/habits/{habitId}")
    private @ResponseBody ResponseEntity<HabitDto> update(@PathVariable("habitId") String habitId, @RequestBody org.springframework.hateoas.Resource<Habit> input) {
        Habit retrievedHabit = habitService.get(Long.parseLong(habitId));

        if (!isCurrentUserHabit(retrievedHabit)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Habit inputHabit = input.getContent();
        publisher.publishEvent(new BeforeSaveEvent(inputHabit));
        propagatePut(inputHabit, retrievedHabit);
        habitService.update(retrievedHabit);
        publisher.publishEvent(new AfterSaveEvent(inputHabit));
        return ResponseEntity.ok(habitMapper.pojoToDto(inputHabit));
    }

    @PutMapping("/habits/{habitId}/complete")
    private @ResponseBody ResponseEntity<HabitDto> complete(@PathVariable("habitId") String habitId) {
        long id = Long.parseLong(habitId);
        Habit retrievedHabit = habitService.get(id);

        if (!isCurrentUserHabit(retrievedHabit)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        publisher.publishEvent(new BeforeSaveEvent(retrievedHabit));
        habitService.complete(id);
        publisher.publishEvent(new AfterSaveEvent(retrievedHabit));
        return ResponseEntity.ok(habitMapper.pojoToDto(retrievedHabit));
    }

    @GetMapping("habits/{habitId}/histories")
    private @ResponseBody ResponseEntity<PageableResource> getHabitHistories(@PathVariable("habitId") Long habitId,
                                                                             @RequestParam(value="page", defaultValue="0") Integer page,
                                                                             @RequestParam(value="size", defaultValue="10") Integer size) {
        String methodPath = linkToHabitHistories(habitId).toString();
        Page<HabitHistory> habitHistories = habitHistoryService.getHabitHistories(habitId, page, size);
        if (!habitHistories.hasContent()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(new PageableResourceAssembler<>(historyMapper, methodPath, null).toResource(habitHistories), HttpStatus.OK);
    }

    private boolean isCurrentUserHabit(Habit habit) {
        return habit.getHabitUser().getUsername().equals(userService.getCurrent().getUsername());
    }

    private void propagatePut(Habit in, Habit out) {
        out.setDescription(in.getDescription());
        out.setDifficulty(in.getDifficulty());
        out.setName(in.getName());
    }
}