package com.olts.discipline.rest.api;

import com.olts.discipline.api.service.UserService;
import com.olts.discipline.entity.Habit;
import com.olts.discipline.entity.User;
import com.olts.discipline.rest.dto.UserGETDto;
import com.olts.discipline.rest.mapper.UserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * OLTS on 06.08.2017.
 */
@RestController
@RequestMapping("/api/users")
class UserRestController {

    @Resource
    private UserService userService;
    @Resource(name = "userMapper")
    private UserMapper userMapper;

    @GetMapping("/{userId}")
    private UserGETDto get(@PathVariable Long userId) {
        return userMapper.userToUserGetDto(userService.get(userId));
    }

    @GetMapping
    private UserGETDto getCurrent() {
        User current = userService.getCurrent();
        return userMapper.userToUserGetDto(current);
    }

    @GetMapping("/habits/{userId}")
    private Collection<Habit> getUserHabits(@PathVariable Long userId) {
        return userService.getNotCompletedUserHabits(userId);
    }
}