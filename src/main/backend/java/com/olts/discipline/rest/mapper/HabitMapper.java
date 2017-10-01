package com.olts.discipline.rest.mapper;

import com.olts.discipline.entity.habit.Habit;
import com.olts.discipline.rest.dto.HabitDto;

/**
 * OLTS on 03.09.2017.
 */
public interface HabitMapper extends PojoToDtoMapper<Habit, HabitDto> {
    Habit habitDtoToHabit(HabitDto habitDto);
}
