package com.olts.discipline.logic.calculator;

import com.olts.discipline.Constants;
import com.olts.discipline.api.service.UserService;
import com.olts.discipline.entity.Habit;
import com.olts.discipline.entity.User;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * OLTS on 11.09.2017.
 */
@Component
public class UserScoreCalculator {

    @Resource
    private UserService userService;

    public void calculate(Habit habit) {
        User user = habit.getHabitUser();
        if (habit.isCompleted()) {
            int score;
            if (habit.isAchieved()) {
                score = user.getHabitScore() + habit.getDifficulty() - (habit.getCompletedCount() / Constants.HABIT_BORDER_COUNT);
            } else {
                score = user.getHabitScore() + habit.getDifficulty();
            }
            user.setHabitScore(score);
        } else {
            if (habit.isAchieved()) {
                return;
            }
            Double score = Math.ceil(habit.getNonCompletedCount() * ((float) habit.getDifficulty() / Constants.HABIT_BORDER_COUNT));
            user.setHabitScore(user.getHabitScore() - score.intValue());
        }
        userService.update(user);
    }
}
