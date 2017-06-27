package com.olts.discipline.rest.api;

import com.olts.discipline.api.repository.HabitRepository;
import com.olts.discipline.model.Habit;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.rest.core.event.AfterSaveEvent;
import org.springframework.data.rest.core.event.BeforeSaveEvent;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * OLTS on 28.05.2017.
 */
@RepositoryRestController
public class RestHabitController implements ApplicationEventPublisherAware {

    @Resource
    private HabitRepository repository;
    private ApplicationEventPublisher publisher;


    @PutMapping("/habits/{habitId}")
    public @ResponseBody ResponseEntity<?> update(@PathVariable("habitId") String habitId, @RequestBody org.springframework.hateoas.Resource<Habit> input) {
        org.springframework.security.core.userdetails.User principal = getPrincipal();
        Habit retrievedHabit = repository.findOne(Long.parseLong(habitId));
        if (!isValid(principal, retrievedHabit)) { return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); }
        Habit habitForPut = input.getContent();
        publisher.publishEvent(new BeforeSaveEvent(habitForPut));
        propagateHabit(retrievedHabit, habitForPut);
        publisher.publishEvent(new AfterSaveEvent(habitForPut));
        return ResponseEntity.ok(habitForPut);
    }

    private User getPrincipal() {
        return (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    // todo refactor
    private void propagateHabit(Habit retrievedHabit, Habit habitForPut) {
        retrievedHabit.setDescription(habitForPut.getDescription());
        retrievedHabit.setDifficulty(habitForPut.getDifficulty());
        retrievedHabit.setName(habitForPut.getName());
        repository.save(retrievedHabit);
    }

    protected boolean isValid(User principal, Habit one) {
        return one.getHabitUser().getUsername().equals(principal.getUsername());
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
}