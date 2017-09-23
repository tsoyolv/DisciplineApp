package com.olts.discipline.api.service.impl;

import com.olts.discipline.api.repository.MessageRepository;
import com.olts.discipline.api.service.ChallengeService;
import com.olts.discipline.api.service.MessageService;
import com.olts.discipline.api.service.UserService;
import com.olts.discipline.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * OLTS on 22.09.2017.
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageRepository repository;
    @Resource
    private UserService userService;
    @Resource
    private ChallengeService challengeService;

    @Override
    public Page<Message> get(Integer page, Integer size) {
        return repository.findAll(new PageRequest(page, size));
    }

    @Override
    public Page<Message> getByChallengeId(Long challengeId, Integer page, Integer size) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public Message get(Long id) {
        return repository.getOne(id);
    }

    @Override
    public Message send(Long userId, Long challengeId, String message) {
        Message messageEntity = new Message();
        messageEntity.setMessage(message);
        messageEntity.setMessageChallenge(challengeService.get(challengeId));
        messageEntity.setMessageUser(userService.get(userId));
        return repository.save(messageEntity);
    }

    @Override
    public Message update(Message message) {
        return repository.save(message);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }
}