package com.olts.discipline.rest.mapper.impl;

import com.olts.discipline.entity.*;
import com.olts.discipline.rest.api.UserRestController;
import com.olts.discipline.rest.dto.UserGETDto;
import com.olts.discipline.rest.dto.UserPutDto;
import com.olts.discipline.rest.mapper.UserMapper;
import org.springframework.hateoas.EntityLinks;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * OLTS on 25.08.2017.
 */
/* MapStruct doesn't work with lombok for entity (only for dto) */
@Component("userMapper")
public class UserMapperImpl implements UserMapper {

    @Resource
    private EntityLinks entityLinks;

    @Override
    public UserGETDto pojoToDto(User obj) {
        UserGETDto dto = new UserGETDto();
        dto.setUsername(obj.getUsername());
        dto.setFirstName(obj.getFirstName());
        dto.setSecondName(obj.getSecondName());
        dto.setLastName(obj.getLastName());
        dto.setEmail(obj.getEmail());
        dto.setHidden(obj.getHidden());
        dto.setLevel(obj.getLevel());
        dto.setLevelPercentage(obj.getLevelPercentage());
        dto.setProgressPerDay(obj.getProgressPerDay());
        dto.setScore(obj.getScore());
        dto.setHabitScore(obj.getHabitScore());
        dto.setTaskScore(obj.getTaskScore());
        dto.setCreatedWhen(obj.getCreatedWhen());
        dto.setRank(obj.getRank());
        dto.setBirthDate(obj.getBirthDate());
        dto.setCity(obj.getCity());
        dto.setCountry(obj.getCountry());
        //private Set<Task> tasks = new HashSet<>();
        dto.add(UserRestController.linkToAvailableUserChallenges(obj.getId()).withRel("challenges"));
        dto.add(UserRestController.linkToUserHabits(obj.getId()).withRel("habits"));
        dto.add(UserRestController.linkToUserGroups(obj.getId()).withRel("groups"));
        dto.add(entityLinks.linkForSingleResource(User.class, obj.getId()).withSelfRel());
        return dto;
    }

    @Override // todo
    public User userGETDtouser(UserGETDto userGETDto) {
        return null;
    }

    @Override
    public UserPutDto userToUserPutDto(User user) {
        UserPutDto userPutDto = new UserPutDto();
        userPutDto.setUsername(user.getUsername());
        userPutDto.setFirstName(user.getFirstName());
        userPutDto.setSecondName(user.getSecondName());
        userPutDto.setLastName(user.getLastName());
        userPutDto.setEmail(user.getEmail());
        userPutDto.setHidden(user.getHidden());
        userPutDto.setBirthDate(user.getBirthDate());
        userPutDto.setCity(user.getCity());
        userPutDto.setCountry(user.getCountry());
        return userPutDto;
    }

    @Override
    public User userPutDtoToUser(UserPutDto userPutDto) {
        return null;
    }
}
