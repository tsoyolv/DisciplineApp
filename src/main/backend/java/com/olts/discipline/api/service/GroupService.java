package com.olts.discipline.api.service;

import com.olts.discipline.entity.Group;
import org.springframework.data.domain.Page;

/**
 * OLTS on 16.09.2017.
 */
public interface GroupService {
    Page<Group> get(Integer page, Integer size);
    Page<Group> getByUserId(Long userId, Integer page, Integer size);
    Group get(Long id);
    Group create(Group group);
    Group update(Group group);
    void delete(Long id);
}