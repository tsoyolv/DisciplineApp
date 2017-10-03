package org.discipline.rest.mapper;

import org.discipline.entity.habit.Habit;
import org.discipline.rest.dto.HabitDto;

/**
 * OLTS on 03.09.2017.
 */
public interface HabitMapper extends PojoToDtoMapper<Habit, HabitDto> {
    Habit habitDtoToHabit(HabitDto habitDto);
}
