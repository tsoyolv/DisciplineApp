package com.olts.discipline.rest.api;

import com.olts.discipline.api.repository.HabitRepository;
import com.olts.discipline.api.service.UserService;
import com.olts.discipline.model.Habit;
import com.olts.discipline.model.User;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.rest.core.event.AfterSaveEvent;
import org.springframework.data.rest.core.event.BeforeSaveEvent;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * OLTS on 28.05.2017.
 */
@RepositoryRestController
class HabitRestController implements ApplicationEventPublisherAware {

    @Resource
    private UserService userService;

    @Resource
    private HabitRepository repository;

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @GetMapping("/habits/user")
    private @ResponseBody ResponseEntity<List<Habit>> getHabitsByCurrentUser() {
        return getHabitsByUser(userService.getCurrent().getId());
    }

    /* todo maybe move it to user? */
    @GetMapping("/habits/user/{userId}")
    @ResponseBody
    private ResponseEntity<List<Habit>> getHabitsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(repository.findByHabitUserId(userId));
    }

    /**
     * merge entities
     * */
    @PutMapping("/habits/{habitId}")
    private @ResponseBody ResponseEntity<?> update(@PathVariable("habitId") String habitId, @RequestBody org.springframework.hateoas.Resource<Habit> input) {
        Habit retrievedHabit = repository.findOne(Long.parseLong(habitId));

        if (!isValid(userService.getCurrent(), retrievedHabit)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Habit inputHabit = input.getContent();
        publisher.publishEvent(new BeforeSaveEvent(inputHabit));
        propagateHabit(inputHabit, retrievedHabit);
        publisher.publishEvent(new AfterSaveEvent(inputHabit));
        return ResponseEntity.ok(inputHabit);
    }

    private void propagateHabit(Habit habitForPut, Habit retrievedHabit) {
        retrievedHabit.setDescription(habitForPut.getDescription());
        retrievedHabit.setDifficulty(habitForPut.getDifficulty());
        retrievedHabit.setName(habitForPut.getName());
        repository.save(retrievedHabit);
    }

    private boolean isValid(User user, Habit habit) {
        return habit.getHabitUser().getUsername().equals(user.getUsername());
    }
}