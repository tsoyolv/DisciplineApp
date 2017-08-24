package com.olts.discipline.api.service.impl;

import com.olts.discipline.api.repository.HabitRepository;
import com.olts.discipline.api.service.HabitHistoryService;
import com.olts.discipline.api.service.HabitService;
import com.olts.discipline.api.service.UserScoreRecalculationService;
import com.olts.discipline.model.Habit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * OLTS on 20.08.2017.
 */
@Service
public class HabitServiceImpl implements HabitService {
    @Resource
    private HabitRepository repository;
    @Resource
    private HabitHistoryService historyService;
    @Resource
    private UserScoreRecalculationService recalculationService;

    @Override
    public Habit get(long id) {
        return repository.findOne(id);
    }

    @Override
    public void complete(long id) {
        Habit completed = repository.findOne(id);
        recalculationService.recalculateScores(completed.getHabitUser().getId(), completed);
        completed.setCompleted(true);
        completed.setCompletedCount(completed.getCompletedCount() + 1);
        repository.save(completed);
        historyService.create(completed);
    }
}
