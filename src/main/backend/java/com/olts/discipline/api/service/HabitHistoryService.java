package com.olts.discipline.api.service;

import com.olts.discipline.entity.Habit;
import com.olts.discipline.entity.HabitHistory;

/**
 * OLTS on 20.08.2017.
 */
public interface HabitHistoryService {
    HabitHistory create(Habit originalHabit);
}
