package com.olts.discipline.api.service;

import com.olts.discipline.model.Habit;
import com.olts.discipline.model.HabitHistory;

/**
 * OLTS on 20.08.2017.
 */
public interface HabitHistoryService {
    HabitHistory create(Habit originalHabit);
}
