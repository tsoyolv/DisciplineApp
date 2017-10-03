package com.olts.discipline.rest.mapper.impl;

import com.olts.discipline.entity.habit.Habit;
import com.olts.discipline.entity.User;
import com.olts.discipline.rest.api.HabitRestController;
import com.olts.discipline.rest.dto.HabitDto;
import com.olts.discipline.rest.mapper.HabitMapper;
import org.springframework.hateoas.EntityLinks;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * OLTS on 03.09.2017.
 */
@Component("habitMapper")
public class HabitMapperImpl implements HabitMapper {

    @Resource
    private EntityLinks entityLinks;

    @Override
    public HabitDto pojoToDto(Habit obj) {
        HabitDto dto = new HabitDto();
        dto.setName(obj.getName());
        dto.setDifficulty(obj.getDifficulty());
        dto.setDescription(obj.getDescription());
        dto.setCompleted(obj.isCompleted());
        dto.setCompletedCount(obj.getCompletedCount());
        dto.setNonCompletedCount(obj.getNonCompletedCount());
        dto.setAchieved(obj.isAchieved());
        dto.setUpdatedWhen(obj.getUpdatedWhen());
        dto.setCreatedWhen(obj.getCreatedWhen());
        dto.add(HabitRestController.linkToHabitHistories(obj.getId()).withRel("histories"));
        dto.add(entityLinks.linkForSingleResource(User.class, obj.getHabitUser().getId()).withRel("habitUser"));
        dto.add(entityLinks.linkForSingleResource(Habit.class, obj.getId()).withSelfRel());
        return dto;
    }

    @Override
    public Habit habitDtoToHabit(HabitDto habitDto) {
        return null;
    }
}
