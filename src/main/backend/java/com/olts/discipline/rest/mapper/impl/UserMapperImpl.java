package com.olts.discipline.rest.mapper.impl;

import com.olts.discipline.entity.User;
import com.olts.discipline.rest.dto.UserGETDto;
import com.olts.discipline.rest.dto.UserPOSTDto;
import com.olts.discipline.rest.mapper.UserMapper;
import org.springframework.stereotype.Component;

/**
 * OLTS on 25.08.2017.
 */
/* MapStruct doesn't work with lombok for entity (only for dto) */
@Component("userMapper")
public class UserMapperImpl implements UserMapper {

    @Override
    public UserGETDto userToUserGetDto(User user) {
        UserGETDto userGETDto = new UserGETDto();
        userGETDto.setId(user.getId());
        userGETDto.setUsername(user.getUsername());
        userGETDto.setFirstName(user.getFirstName());
        userGETDto.setSecondName(user.getSecondName());
        userGETDto.setLastName(user.getLastName());
        userGETDto.setEmail(user.getEmail());
        userGETDto.setIsHidden(user.getIsHidden());
        userGETDto.setLevel(user.getLevel());
        userGETDto.setLevelPercentage(user.getLevelPercentage());
        userGETDto.setProgressPerDay(user.getProgressPerDay());
        userGETDto.setScore(user.getScore());
        userGETDto.setHabitScore(user.getHabitScore());
        userGETDto.setTaskScore(user.getTaskScore());
        userGETDto.setCreatedWhen(user.getCreatedWhen());
        return userGETDto;
    }

    @Override // todo
    public User userGETDtouser(UserGETDto userGETDto) {
        return null;
    }

    @Override
    public UserPOSTDto userToUserPostDto(User user) {
        return null;
    }

    @Override
    public User userPostDtoToUser(UserPOSTDto userPOSTDto) {
        return null;
    }
}
