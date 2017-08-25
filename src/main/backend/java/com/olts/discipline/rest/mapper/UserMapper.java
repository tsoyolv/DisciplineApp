package com.olts.discipline.rest.mapper;

import com.olts.discipline.entity.User;
import com.olts.discipline.rest.dto.UserGETDto;
import com.olts.discipline.rest.dto.UserPOSTDto;

/**
 * OLTS on 25.08.2017.
 */
public interface UserMapper {
    UserGETDto userToUserGetDto(User user);

    User userGETDtouser(UserGETDto userGETDto);

    UserPOSTDto userToUserPostDto(User user);

    User userPostDtoToUser(UserPOSTDto userPOSTDto);
}
