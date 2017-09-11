package com.olts.discipline.logic;

import com.olts.discipline.Constants;
import com.olts.discipline.api.service.HabitHistoryService;
import com.olts.discipline.api.service.HabitService;
import com.olts.discipline.entity.Habit;
import com.olts.discipline.logic.calculator.UserScoreCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * OLTS on 11.09.2017.
 */
@Component
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private static final int PAGE_SIZE = 1000;
    @Resource
    private HabitService habitService;
    @Resource
    private HabitHistoryService historyService;
    @Resource
    private UserScoreCalculator userScoreCalculator;

    @Scheduled(cron = "0 * * * * ?") // every minute
    //@Scheduled(cron = "0 0 12 * * ?") // night
    public void processHabits() {
        log.info("Habit activity start {}", dateFormat.format(new Date()));
        processNonCompletedHabits(); // !! don't change order
        processCompletedHabits();
        log.info("Habit activity end {}", dateFormat.format(new Date()));
    }

    private void processNonCompletedHabits() {
        processHabitsForCompletion(false);
    }

    private void processCompletedHabits() {
        processHabitsForCompletion(true);
    }

    private void processHabitsForCompletion(boolean completed) {
        int pageNum = 0;
        Page<Habit> page = habitService.get(false, completed, new PageRequest(pageNum, PAGE_SIZE));
        while (page.hasContent()) {
            page.getContent().forEach(this::processHabit);
            page = habitService.get(true, false, new PageRequest(++pageNum, PAGE_SIZE));
        }
    }

    private void processHabit(Habit habit) {
        if (habit.isCompleted()) {
            habit.setCompleted(false);
            habitService.update(habit);
            // there is no need to calculate score and write history. is has been implemented in habitService.complete
        } else {
            if (habit.getNonCompletedCount() == Constants.HABIT_BORDER_COUNT) {
                failHabit(habit);
                return;
            }
            habit.setNonCompletedCount(habit.getNonCompletedCount() + 1);
            habitService.update(habit);
            userScoreCalculator.calculate(habit);
            historyService.create(habit);
        }
    }

    private void failHabit(Habit habit) {

    }
}