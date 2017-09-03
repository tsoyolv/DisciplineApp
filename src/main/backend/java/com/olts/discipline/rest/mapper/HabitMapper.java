package com.olts.discipline.rest.mapper;

import com.olts.discipline.entity.Habit;
import com.olts.discipline.rest.dto.HabitDto;

/**
 * OLTS on 03.09.2017.
 */
public interface HabitMapper {
    HabitDto habitToHabitDto(Habit habit);
    Habit habitDtoToHabit(HabitDto habitDto);
}
