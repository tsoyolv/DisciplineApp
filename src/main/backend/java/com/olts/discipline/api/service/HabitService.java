package com.olts.discipline.api.service;

import com.olts.discipline.entity.Habit;

import java.util.List;

/**
 * OLTS on 20.08.2017.
 */
public interface HabitService {
    Habit get(long id);

    List<Habit> getByUserId(Long userId);

    void complete(long id);
}
