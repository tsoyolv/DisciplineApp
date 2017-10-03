package org.discipline.rest.mapper;

import org.discipline.entity.User;
import org.discipline.rest.dto.UserGETDto;
import org.discipline.rest.dto.UserPutDto;

/**
 * OLTS on 25.08.2017.
 */
public interface UserMapper extends PojoToDtoMapper<User, UserGETDto> {
    User userGETDtouser(UserGETDto userGETDto);

    UserPutDto userToUserPutDto(User user);

    User userPutDtoToUser(UserPutDto userPutDto);
}