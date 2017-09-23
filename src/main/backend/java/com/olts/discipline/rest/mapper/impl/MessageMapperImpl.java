package com.olts.discipline.rest.mapper.impl;

import com.olts.discipline.entity.Challenge;
import com.olts.discipline.entity.Message;
import com.olts.discipline.entity.User;
import com.olts.discipline.rest.dto.MessageDto;
import com.olts.discipline.rest.mapper.MessageMapper;
import org.springframework.hateoas.EntityLinks;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * OLTS on 23.09.2017.
 */
@Component
public class MessageMapperImpl implements MessageMapper {

    @Resource
    private EntityLinks entityLinks;


    @Override
    public MessageDto pojoToDto(Message message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage(message.getMessage());
        messageDto.setUsername(message.getMessageUser().getUsername());
        messageDto.setWasSent(message.getWasSent());
        messageDto.add(entityLinks.linkForSingleResource(User.class, message.getMessageUser().getId()).withRel("messageUser"));
        messageDto.add(entityLinks.linkForSingleResource(Challenge.class, message.getMessageChallenge().getId()).withRel("messageChallenge"));
        messageDto.add(entityLinks.linkForSingleResource(Message.class, message.getId()).withSelfRel());
        return messageDto;
    }
}

