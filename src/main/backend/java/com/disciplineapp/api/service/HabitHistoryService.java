package com.disciplineapp.api.service;

import com.disciplineapp.entity.habit.Habit;
import com.disciplineapp.entity.habit.HabitHistory;
import org.springframework.data.domain.Page;

/**
 * OLTS on 20.08.2017.
 */
public interface HabitHistoryService {
    Page<HabitHistory> getHabitHistories(Long habitId, int page, int size);
    HabitHistory create(Habit originalHabit);
}
