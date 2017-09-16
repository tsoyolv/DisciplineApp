package com.olts.discipline.rest.mapper.impl;

import com.olts.discipline.entity.Group;
import com.olts.discipline.rest.dto.GroupDto;
import com.olts.discipline.rest.mapper.GroupMapper;
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
        // private User createdBy; link
        //private List<User> users; link
        return dto;
    }
}
