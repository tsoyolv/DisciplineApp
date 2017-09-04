package com.olts.discipline.rest.mapper.impl;

import com.olts.discipline.entity.Habit;
import com.olts.discipline.entity.User;
import com.olts.discipline.rest.dto.HabitDto;
import com.olts.discipline.rest.mapper.HabitMapper;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkBuilder;
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
    public HabitDto pojoToDto(Habit habit) {
        HabitDto habitDto = new HabitDto();
        habitDto.setName(habit.getName());
        habitDto.setDifficulty(habit.getDifficulty());
        habitDto.setDescription(habit.getDescription());
        habitDto.setCompleted(habit.isCompleted());
        habitDto.setCompletedCount(habit.getCompletedCount());
        habitDto.setAchieved(habit.isAchieved());
        habitDto.setUpdatedWhen(habit.getUpdatedWhen());
        habitDto.setCreatedWhen(habit.getCreatedWhen());
        LinkBuilder linkBuilder = entityLinks.linkForSingleResource(User.class, habit.getHabitUser().getId());
        habitDto.add(linkBuilder.withRel("habitUser"));
        Link link = entityLinks.linkForSingleResource(Habit.class, habit.getId()).withSelfRel();
        habitDto.add(link);
        return habitDto;
    }

    @Override
    public Habit habitDtoToHabit(HabitDto habitDto) {
        return null;
    }
}
