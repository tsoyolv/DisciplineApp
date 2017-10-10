package com.disciplineapp.rest.mapper;

import com.disciplineapp.rest.dto.HabitDto;
import com.disciplineapp.entity.habit.Habit;

/**
 * OLTS on 03.09.2017.
 */
public interface HabitMapper extends PojoToDtoMapper<Habit, HabitDto> {
    Habit habitDtoToHabit(HabitDto habitDto);
}
