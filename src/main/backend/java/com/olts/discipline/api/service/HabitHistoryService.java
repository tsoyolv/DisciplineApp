package com.olts.discipline.api.service;

import com.olts.discipline.entity.habit.Habit;
import com.olts.discipline.entity.habit.HabitHistory;
import org.springframework.data.domain.Page;

/**
 * OLTS on 20.08.2017.
 */
public interface HabitHistoryService {
    Page<HabitHistory> getHabitHistories(Long habitId, int page, int size);
    HabitHistory create(Habit originalHabit);
}
