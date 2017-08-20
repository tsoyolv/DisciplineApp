package com.olts.discipline.api.service;


import com.olts.discipline.model.Activity;
import com.olts.discipline.model.Habit;
import com.olts.discipline.model.User;

import java.util.Collection;

public interface UserService {
    void save(User user);

    User get(long userId);

    User getByUsername(String username);

    User getCurrent();

    Collection<Habit> getUserHabits(long userId);

    void recalculateScores(long userId, Activity activity);
}
