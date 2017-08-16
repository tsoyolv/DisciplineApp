package com.olts.discipline.rest.api;

import com.olts.discipline.api.service.UserService;
import com.olts.discipline.model.Habit;
import com.olts.discipline.model.User;
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
    UserService userService;

    @GetMapping("/{userId}")
    User get(@PathVariable Long userId) {
        return userService.get(userId);
    }

    @GetMapping("")
    User getCurrent() {
        return userService.getCurrent();
    }


    @GetMapping("/habits/{userId}")
    Collection<Habit> getUserHabits(@PathVariable Long userId) {
        return userService.getUserHabits(userId);
    }
}