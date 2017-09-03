package com.olts.discipline.rest.mapper.impl;

import com.olts.discipline.entity.Habit;
import com.olts.discipline.entity.User;
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
    public HabitDto habitToHabitDto(Habit habit) {
        HabitDto habitDto = new HabitDto();
        habitDto.setName(habit.getName());
        habitDto.setDifficulty(habit.getDifficulty());
        habitDto.setDescription(habit.getDescription());
        habitDto.setCompleted(habit.isCompleted());
        habitDto.setCompletedCount(habit.getCompletedCount());
        habitDto.setAchieved(habit.isAchieved());
        habitDto.setUpdatedWhen(habit.getUpdatedWhen());
        habitDto.setCreatedWhen(habit.getCreatedWhen());
        habitDto.add(entityLinks.linkForSingleResource(User.class, habit.getHabitUser().getId()).withRel("habitUser"));
        return habitDto;
    }

    @Override
    public Habit habitDtoToHabit(HabitDto habitDto) {
        return null;
    }
}
