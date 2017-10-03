package com.disciplineapp.rest.api;

import com.disciplineapp.entity.Group;
import com.disciplineapp.entity.User;
import com.disciplineapp.rest.hateoas.assembler.PageableResourceAssembler;
import com.disciplineapp.rest.mapper.GroupMapper;
import com.disciplineapp.api.service.GroupService;
import com.disciplineapp.api.service.UserService;
import com.disciplineapp.rest.dto.GroupDto;
import com.disciplineapp.rest.hateoas.PageableResource;
import com.disciplineapp.rest.mapper.UserMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.domain.Page;
import org.springframework.data.rest.core.event.AfterSaveEvent;
import org.springframework.data.rest.core.event.BeforeSaveEvent;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * OLTS on 16.09.2017.
 */
@RepositoryRestController
public class GroupRestController implements ApplicationEventPublisherAware {
    public static ControllerLinkBuilder linkToGroupUsers(Long groupId) {
        return ControllerLinkBuilder.linkTo(GroupRestController.class).slash(String.format("api/groups/%x/users", groupId));
    }
    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @Resource
    private GroupService groupService;
    @Resource
    private UserService userService;

    @Resource
    private GroupMapper mapper;
    @Resource
    private UserMapper userMapper;


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

    @GetMapping("/groups/{groupId}/users")
    private @ResponseBody ResponseEntity<PageableResource> getUserGroups(
            @PathVariable("groupId") Long groupId,
            @RequestParam(value="page", defaultValue="0") Integer page,
            @RequestParam(value="size", defaultValue="10") Integer size) {
        String methodPath = linkToGroupUsers(groupId).toString();
        Page<User> users = userService.getByGroup(groupId, page, size);
        return new ResponseEntity<>(new PageableResourceAssembler<>(userMapper, methodPath, null).toResource(users), HttpStatus.OK);
    }
}
