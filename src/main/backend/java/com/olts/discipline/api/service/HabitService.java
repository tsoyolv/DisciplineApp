package com.olts.discipline.api.service;

import com.olts.discipline.model.Habit;

import java.util.Collection;

/**
 * OLTS on 20.08.2017.
 */
public interface HabitService {
    Habit get(long id);

    Collection<Habit> getNotCompletedUserHabits(long userId);

    void complete(long id);
}
