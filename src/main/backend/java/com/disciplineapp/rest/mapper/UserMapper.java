package com.disciplineapp.rest.mapper;

import com.disciplineapp.entity.User;
import com.disciplineapp.rest.dto.UserGETDto;
import com.disciplineapp.rest.dto.UserPutDto;

/**
 * OLTS on 25.08.2017.
 */
public interface UserMapper extends PojoToDtoMapper<User, UserGETDto> {
    User userGETDtouser(UserGETDto userGETDto);

    UserPutDto userToUserPutDto(User user);

    User userPutDtoToUser(UserPutDto userPutDto);
}