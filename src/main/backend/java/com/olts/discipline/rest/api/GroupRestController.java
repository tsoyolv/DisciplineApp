package com.olts.discipline.rest.api;

import com.olts.discipline.api.service.GroupService;
import com.olts.discipline.entity.Group;
import com.olts.discipline.entity.User;
import com.olts.discipline.rest.dto.GroupDto;
import com.olts.discipline.rest.mapper.GroupMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.rest.core.event.AfterSaveEvent;
import org.springframework.data.rest.core.event.BeforeSaveEvent;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * OLTS on 16.09.2017.
 */
@RepositoryRestController
class GroupRestController implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @Resource
    private GroupService groupService;
    @Resource
    private GroupMapper mapper;

    @PostMapping("/groups/")
    private @ResponseBody
    ResponseEntity<GroupDto> create(@RequestBody org.springframework.hateoas.Resource<Group> input) {
        Group group = input.getContent();
        User creator = group.getCreatedBy();
        if ((creator.getAllowedGroups() + 1) > creator.getLevel()) {
            return ResponseEntity.badRequest().build();
        }

        publisher.publishEvent(new BeforeSaveEvent(group));
        Group created = groupService.create(group);
        publisher.publishEvent(new AfterSaveEvent(group));
        return ResponseEntity.ok(mapper.pojoToDto(created));
    }
}
