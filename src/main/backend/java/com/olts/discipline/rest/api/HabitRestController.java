package com.olts.discipline.rest.api;

import com.olts.discipline.api.service.HabitService;
import com.olts.discipline.api.service.UserService;
import com.olts.discipline.entity.Habit;
import com.olts.discipline.rest.dto.HabitDto;
import com.olts.discipline.rest.mapper.HabitMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.rest.core.event.AfterSaveEvent;
import org.springframework.data.rest.core.event.BeforeSaveEvent;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * OLTS on 28.05.2017.
 */
@RepositoryRestController
class HabitRestController implements ApplicationEventPublisherAware {

    @Resource
    private UserService userService;

    @Resource
    private HabitService habitService;
    @Resource
    private HabitMapper habitMapper;

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

    private boolean isCurrentUserHabit(Habit habit) {
        return habit.getHabitUser().getUsername().equals(userService.getCurrent().getUsername());
    }

    private void propagatePut(Habit in, Habit out) {
        out.setDescription(in.getDescription());
        out.setDifficulty(in.getDifficulty());
        out.setName(in.getName());
    }
}