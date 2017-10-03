package org.discipline.api.handler;

import com.olts.discipline.api.service.UserService;
import com.olts.discipline.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.olts.discipline.configuration.WebSocketConfiguration.MESSAGE_PREFIX;

/**
 * OLTS on 23.09.2017.
 */
@Component
@RepositoryEventHandler(Message.class)
public class MessageEventHandler {

    private final SimpMessagingTemplate websocket;

    private final EntityLinks entityLinks;

    @Resource
    private UserService userService;

    @Autowired
    public MessageEventHandler(SimpMessagingTemplate websocket, EntityLinks entityLinks) {
        this.websocket = websocket;
        this.entityLinks = entityLinks;
    }

    /*@HandleBeforeCreate
    public void applyUserInformationUsingSecurityContext(Message message) {
        message.setMessageUser(userService.getCurrent());
    }*/

    @HandleAfterCreate
    public void newMessage(Message message) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/newMessage", getPath(message));
    }

    @HandleAfterDelete
    public void deleteMessage(Message message) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/deleteMessage", getPath(message));
    }

    @HandleAfterSave
    public void updateMessage(Message message) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/updateMessage", getPath(message));
    }

    /**
     * Take an {@link Message} and get the URI using Spring Data REST's {@link EntityLinks}.
     *
     * @param message
     */
    private String getPath(Message message) {
        return this.entityLinks.linkForSingleResource(message.getClass(),
                message.getId()).toUri().getPath();
    }

}