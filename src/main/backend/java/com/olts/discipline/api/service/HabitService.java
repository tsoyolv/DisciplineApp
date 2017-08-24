package com.olts.discipline.api.service;

import com.olts.discipline.model.Habit;

/**
 * OLTS on 20.08.2017.
 */
public interface HabitService {
    Habit get(long id);

    void complete(long id);
}
