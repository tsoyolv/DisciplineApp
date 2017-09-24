package com.olts.discipline.api.service.impl;

import com.olts.discipline.api.repository.GroupRepository;
import com.olts.discipline.api.service.GroupService;
import com.olts.discipline.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * OLTS on 16.09.2017.
 */
@Service
public class GroupServiceImpl implements GroupService {
    @Resource
    private GroupRepository repository;

    @Override
    public Page<Group> get(Integer page, Integer size) {
        return repository.findAll(new PageRequest(page, size, Sort.Direction.ASC, "name"));
    }

    @Override
    public Page<Group> getByUserId(Long userId, Integer page, Integer size) {
        return repository.findByUserId(userId, new PageRequest(page, size, Sort.Direction.DESC, "updatedWhen"));
    }

    @Override
    public Group get(Long id) {
        return repository.getOne(id);
    }

    @Override
    public Group create(Group group) {
        return repository.save(group);
    }

    @Override
    public Group update(Group group) {
        return repository.save(group);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }
}
