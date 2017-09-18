package com.olts.discipline.api.handler;

import com.olts.discipline.api.service.UserService;
import com.olts.discipline.entity.UserChallenge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.hateoas.EntityLinks;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.olts.discipline.configuration.WebSocketConfiguration.MESSAGE_PREFIX;

/**
 * OLTS on 18.09.2017.
 */
@Component
@RepositoryEventHandler(UserChallenge.class)
public class UserChallengeEventHandler {

    private final SimpMessagingTemplate websocket;

    private final EntityLinks entityLinks;

    @Resource
    private UserService userService;

    @Autowired
    public UserChallengeEventHandler(SimpMessagingTemplate websocket, EntityLinks entityLinks) {
        this.websocket = websocket;
        this.entityLinks = entityLinks;
    }

    @HandleBeforeCreate
    @HandleBeforeSave
    public void applyUserInformationUsingSecurityContext(UserChallenge userChallenge) {
        //userChallenge.setCreatedBy(userService.getCurrent());
    }

    @HandleAfterCreate
    public void newUserChallenge(UserChallenge userChallenge) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/newUserChallenge", getPath(userChallenge));
    }

    @HandleAfterDelete
    public void deleteUserChallenge(UserChallenge userChallenge) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/deleteUserChallenge", getPath(userChallenge));
    }

    @HandleAfterSave
    public void updateUserChallenge(UserChallenge userChallenge) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/updateUserChallenge", getPath(userChallenge));
    }

    /**
     * Take an {@link UserChallenge} and get the URI using Spring Data REST's {@link EntityLinks}.
     *
     * @param userChallenge
     */
    private String getPath(UserChallenge userChallenge) {
        return this.entityLinks.linkForSingleResource(userChallenge.getClass(),
                userChallenge.getId()).toUri().getPath();
    }

}
