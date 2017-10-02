package org.discipline.logic.calculator;

import com.olts.discipline.entity.Challenge;
import com.olts.discipline.entity.habit.Habit;

/**
 * OLTS on 15.09.2017.
 */
public interface UserScoreCalculator {
    void calculate(Habit habit);
    void calculate(Challenge challenge);
}
