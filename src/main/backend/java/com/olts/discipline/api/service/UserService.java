package com.olts.discipline.api.service;


import com.olts.discipline.entity.Habit;
import com.olts.discipline.entity.User;

import java.util.Collection;

public interface UserService {
    void create(User user);

    void update(User user);

    User get(long userId);

    User getByUsername(String username);

    User getCurrent();

    Collection<Habit> getUserHabits(long userId);

    Collection<Habit> getNotCompletedUserHabits(long userId);

    Collection<Habit> getCompletedUserHabits(long userId);
}
