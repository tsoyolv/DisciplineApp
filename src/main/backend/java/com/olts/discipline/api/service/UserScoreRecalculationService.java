package com.olts.discipline.api.service;

import com.olts.discipline.model.Challenge;
import com.olts.discipline.model.Habit;
import com.olts.discipline.model.Task;
import com.olts.discipline.model.User;

/**
 * OLTS on 24.08.2017.
 */
public interface UserScoreRecalculationService {
    void recalculateScores(long userId, Object activity);

    void recalculateChallenge(User user, Challenge challenge);

    void recalculateTask(User user, Task task);

    void recalculateHabit(User user, Habit habit);
}
