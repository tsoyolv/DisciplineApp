package com.disciplineapp.rest.api;

import com.disciplineapp.api.service.MessageService;
import com.disciplineapp.entity.Message;
import com.disciplineapp.rest.dto.MessageDto;
import com.disciplineapp.rest.dto.MessagePostDto;
import com.disciplineapp.rest.mapper.MessageMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.rest.core.event.AfterCreateEvent;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * OLTS on 23.09.2017.
 */

@RepositoryRestController
public class MessageRestController implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @Resource
    private MessageService messageService;
    @Resource
    private MessageMapper mapper;

    @PostMapping("/messages/send/{challengeId}/{userId}")
    private @ResponseBody
    ResponseEntity<MessageDto> send(@PathVariable("userId") Long userId, @PathVariable("challengeId") Long challengeId,
                                    @RequestBody org.springframework.hateoas.Resource<MessagePostDto> input) {
        Message sent = messageService.send(userId, challengeId, input.getContent().getMessage());
        publisher.publishEvent(new AfterCreateEvent(sent));
        return ResponseEntity.ok(mapper.pojoToDto(sent));
    }
}