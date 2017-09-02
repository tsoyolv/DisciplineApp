package com.olts.discipline.rest.api;

import com.olts.discipline.api.service.UserService;
import com.olts.discipline.entity.User;
import com.olts.discipline.rest.dto.UserGETDto;
import com.olts.discipline.rest.mapper.UserMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

import javax.annotation.Resource;

/**
 * OLTS on 06.08.2017.
 */
//@RestController
//@RequestMapping("/api/users")
class NewUserRestController {

    @Resource
    private UserService userService;
    @Resource(name = "userMapper")
    private UserMapper userMapper;

    /*@GetMapping("/{userId}")
    private UserGETDto get(@PathVariable Long userId) {
        return userMapper.userToUserGetDto(userService.get(userId));
    }*/

    //@GetMapping("/current")
    private UserGETDto getCurrent() {
        User current = userService.getCurrent();
        UserGETDto userGETDto = userMapper.userToUserGetDto(current);
        userGETDto.add(new Link(ControllerLinkBuilder.linkTo(NewUserRestController.class).toString() , "profile"));
        return userGETDto;
    }

    /*@GetMapping("/habits/{userId}")
    private Collection<Habit> getUserHabits(@PathVariable Long userId) {
        return userService.getNotCompletedUserHabits(userId);
    }*/
}