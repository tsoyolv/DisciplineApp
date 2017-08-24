package com.olts.discipline.api.service.impl;

import com.olts.discipline.Constants;
import com.olts.discipline.api.service.UserScoreRecalculationService;
import com.olts.discipline.api.service.UserService;
import com.olts.discipline.model.Challenge;
import com.olts.discipline.model.Habit;
import com.olts.discipline.model.Task;
import com.olts.discipline.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * OLTS on 24.08.2017.
 */
@Service
public class UserScoreRecalculationServiceImpl implements UserScoreRecalculationService {

    @Resource
    private UserService userService;

    @Override
    public void recalculateScores(long userId, Object activity) {
        User user = userService.get(userId);
        if (activity instanceof Habit) {
            recalculateHabit(user, (Habit)activity);
        } else if (activity instanceof Task) {
            recalculateTask(user, (Task)activity);
        } else {
            recalculateChallenge(user, (Challenge) activity);
        }
        userService.update(user);
    }

    @Override
    public void recalculateChallenge(User user, Challenge challenge) {

    }

    @Override
    public void recalculateTask(User user, Task task) {

    }

    @Override
    public void recalculateHabit(User user, Habit habit) {
        if (habit.getCompletedCount() >= Constants.HABIT_BORDER_COUNT) {
            increaseUserLevel(user);
            int newScore = habit.getDifficulty() + user.getHabitScore();
        }
    }

    private void increaseUserLevel(User user) {

        user.setLevel(user.getLevel() + 1);
    }
}
