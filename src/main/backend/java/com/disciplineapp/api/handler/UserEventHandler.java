package com.disciplineapp.api.handler;

import com.disciplineapp.configuration.WebSocketConfiguration;
import com.disciplineapp.entity.User;
import com.disciplineapp.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.hateoas.EntityLinks;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * OLTS on 31.08.2017.
 */
@Component
@RepositoryEventHandler(User.class)
public class UserEventHandler {

    private final SimpMessagingTemplate websocket;

    private final EntityLinks entityLinks;

    @Resource
    private UserService userService;

    @Autowired
    public UserEventHandler(SimpMessagingTemplate websocket, EntityLinks entityLinks) {
        this.websocket = websocket;
        this.entityLinks = entityLinks;
    }

    @HandleAfterCreate
    public void newUser(User user) {
        this.websocket.convertAndSend(
                WebSocketConfiguration.MESSAGE_PREFIX + "/newUser", getPath(user));
    }

    @HandleAfterDelete
    public void deleteUser(User user) {
        this.websocket.convertAndSend(
                WebSocketConfiguration.MESSAGE_PREFIX + "/deleteUser", getPath(user));
    }

    @HandleAfterSave
    public void updateUser(User user) {
        this.websocket.convertAndSend(
                WebSocketConfiguration.MESSAGE_PREFIX + "/updateUser", getPath(user));
    }

    /**
     * Take an {@link User} and get the URI using Spring Data REST's {@link EntityLinks}.
     *
     * @param user
     */
    private String getPath(User user) {
        return this.entityLinks.linkForSingleResource(user.getClass(),
                user.getId()).toUri().getPath();
    }

}