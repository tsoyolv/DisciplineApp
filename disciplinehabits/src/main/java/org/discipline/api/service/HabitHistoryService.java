package org.discipline.api.service;

import org.discipline.entity.habit.Habit;
import org.discipline.entity.habit.HabitHistory;
import org.springframework.data.domain.Page;

/**
 * OLTS on 20.08.2017.
 */
public interface HabitHistoryService {
    Page<HabitHistory> getHabitHistories(Long habitId, int page, int size);
    HabitHistory create(Habit originalHabit);
}
