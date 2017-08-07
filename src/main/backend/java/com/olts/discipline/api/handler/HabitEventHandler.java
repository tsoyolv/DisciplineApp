package com.olts.discipline.api.handler;

import com.olts.discipline.api.repository.UserRepository;
import com.olts.discipline.api.service.UserService;
import com.olts.discipline.model.Habit;
import com.olts.discipline.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.hateoas.EntityLinks;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.olts.discipline.configuration.WebSocketConfiguration.MESSAGE_PREFIX;

/**
 * OLTS on 01.06.2017.
 */
@Component
@RepositoryEventHandler(Habit.class)
public class HabitEventHandler {

    private final SimpMessagingTemplate websocket;

    private final EntityLinks entityLinks;

    @Resource
    private UserService userService;

    @Autowired
    public HabitEventHandler(SimpMessagingTemplate websocket, EntityLinks entityLinks) {
        this.websocket = websocket;
        this.entityLinks = entityLinks;
    }

    @HandleBeforeCreate
    @HandleBeforeSave
    public void applyUserInformationUsingSecurityContext(Habit habit) {
        habit.setHabitUser(userService.getCurrent());
    }

    @HandleAfterCreate
    public void newHabit(Habit habit) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/newHabit", getPath(habit));
    }

    @HandleAfterDelete
    public void deleteHabit(Habit habit) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/deleteHabit", getPath(habit));
    }

    @HandleAfterSave
    public void updateHabit(Habit habit) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/updateHabit", getPath(habit));
    }

    /**
     * Take an {@link Habit} and get the URI using Spring Data REST's {@link EntityLinks}.
     *
     * @param habit
     */
    private String getPath(Habit habit) {
        return this.entityLinks.linkForSingleResource(habit.getClass(),
                habit.getId()).toUri().getPath();
    }

}