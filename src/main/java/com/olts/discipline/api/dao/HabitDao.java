package com.olts.discipline.api.dao;

import com.olts.discipline.model.Habit;

import java.util.Collection;

/**
 * o.tsoy
 * 25.04.2017
 */
public interface HabitDao {

    Habit create(Habit template);

    Habit get(long id);

    Collection<Habit> get();

    Habit update(Habit template);

    Habit delete(long id);
}
