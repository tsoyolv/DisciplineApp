package com.disciplineapp.logic.calculator;

import com.disciplineapp.entity.Challenge;
import com.disciplineapp.entity.habit.Habit;

/**
 * OLTS on 15.09.2017.
 */
public interface UserScoreCalculator {
    void calculate(Habit habit);
    void calculate(Challenge challenge);
}
