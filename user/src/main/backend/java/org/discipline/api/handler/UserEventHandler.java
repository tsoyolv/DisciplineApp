package org.discipline.api.handler;

import org.discipline.api.service.UserService;
import org.discipline.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static org.discipline.configuration.WebSocketConfiguration.MESSAGE_PREFIX;

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
                MESSAGE_PREFIX + "/newUser", getPath(user));
    }

    @HandleAfterDelete
    public void deleteUser(User user) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/deleteUser", getPath(user));
    }

    @HandleAfterSave
    public void updateUser(User user) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/updateUser", getPath(user));
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