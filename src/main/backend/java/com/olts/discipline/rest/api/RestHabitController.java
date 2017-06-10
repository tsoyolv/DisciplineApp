package com.olts.discipline.rest.api;

import com.olts.discipline.api.repository.HabitRepository;
import com.olts.discipline.api.repository.UserRepository;
import com.olts.discipline.model.Habit;
import com.olts.discipline.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.rest.core.event.AfterCreateEvent;
import org.springframework.data.rest.core.event.BeforeCreateEvent;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.Resource;
import java.net.URI;

/**
 * OLTS on 28.05.2017.
 */
@RepositoryRestController
public class RestHabitController implements ApplicationEventPublisherAware {

    private final HabitRepository repository;
    private ApplicationEventPublisher publisher;

    @Autowired
    public RestHabitController(HabitRepository repo) {
        repository = repo;
    }

    @Resource
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.POST, value = "/habits")
    public @ResponseBody ResponseEntity<?> create(
            @RequestBody org.springframework.hateoas.Resource<Habit> input) {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User byUsername = userRepository.findByUsername(principal.getUsername());
        Habit retrievedHabit = input.getContent();
        retrievedHabit.setHabitUser(byUsername);
        publisher.publishEvent(new BeforeCreateEvent(retrievedHabit));
        Habit result = repository.save(retrievedHabit);
        publisher.publishEvent(new AfterCreateEvent(result));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).build();
        //return ResponseEntity.noContent().build();
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
}