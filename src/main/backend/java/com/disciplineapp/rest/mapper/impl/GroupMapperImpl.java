package com.disciplineapp.rest.mapper.impl;

import com.disciplineapp.entity.Group;
import com.disciplineapp.entity.User;
import com.disciplineapp.rest.api.GroupRestController;
import com.disciplineapp.rest.mapper.GroupMapper;
import com.disciplineapp.rest.dto.GroupDto;
import org.springframework.hateoas.EntityLinks;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * OLTS on 16.09.2017.
 */
@Component
public class GroupMapperImpl implements GroupMapper {

    @Resource
    private EntityLinks entityLinks;

    @Override
    public GroupDto pojoToDto(Group obj) {
        GroupDto dto = new GroupDto();
        dto.setName(obj.getName());
        dto.setDescription(obj.getDescription());
        dto.setCreatedWhen(obj.getCreatedWhen());
        dto.setUpdatedWhen(obj.getUpdatedWhen());
        dto.add(GroupRestController.linkToGroupUsers(obj.getId()).withRel("users"));
        dto.add(entityLinks.linkForSingleResource(User.class, obj.getCreatedBy().getId()).withRel("createdBy"));
        dto.add(entityLinks.linkForSingleResource(Group.class, obj.getId()).withSelfRel());
        return dto;
    }
}
