package com.olts.discipline.rest.api;

import com.olts.discipline.api.service.UserService;
import com.olts.discipline.entity.Habit;
import com.olts.discipline.entity.User;
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

    @GetMapping("/{userId}")
    private User get(@PathVariable Long userId) {
        return userService.get(userId);
    }

    @GetMapping
    private User getCurrent() {
        return userService.getCurrent();
    }

    @GetMapping("/habits/{userId}")
    private Collection<Habit> getUserHabits(@PathVariable Long userId) {
        return userService.getNotCompletedUserHabits(userId);
    }
}