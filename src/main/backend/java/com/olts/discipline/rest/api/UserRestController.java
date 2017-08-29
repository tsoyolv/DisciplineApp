package com.olts.discipline.rest.api;

import com.olts.discipline.api.service.UserService;
import com.olts.discipline.rest.dto.UserGETDto;
import com.olts.discipline.rest.mapper.UserMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * OLTS on 29.08.2017.
 */
@RepositoryRestController
class UserRestController implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher publisher;
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @Resource
    private UserService userService;
    @Resource(name = "userMapper")
    private UserMapper userMapper;

    @GetMapping("/users/current")
    private @ResponseBody
    ResponseEntity<UserGETDto> getHabitsByCurrentUser() {
        return ResponseEntity.ok(userMapper.userToUserGetDto(userService.getCurrent()));
    }
}