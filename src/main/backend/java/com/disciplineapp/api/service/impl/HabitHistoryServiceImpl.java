package com.disciplineapp.api.service.impl;

import com.disciplineapp.api.repository.HabitHistoryRepository;
import com.disciplineapp.api.service.HabitHistoryService;
import com.disciplineapp.entity.habit.Habit;
import com.disciplineapp.entity.habit.HabitHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * OLTS on 20.08.2017.
 */
@Service
public class HabitHistoryServiceImpl implements HabitHistoryService {
    @Resource
    private HabitHistoryRepository repository;

    @Override
    public Page<HabitHistory> getHabitHistories(Long habitId, int page, int size) {
        return repository.findByOriginalHabitId(habitId, new PageRequest(page, size, Sort.Direction.DESC, "completedDate"));
    }

    @Override
    public HabitHistory create(Habit originalHabit) {
        HabitHistory history = new HabitHistory();
        propagateHistory(originalHabit, history);
        return repository.save(history);
    }

    private void propagateHistory(Habit originalHabit, HabitHistory history) {
        history.setName(originalHabit.getName());
        history.setCompletedCount(originalHabit.getCompletedCount());
        history.setNonCompletedCount(originalHabit.getNonCompletedCount());
        history.setCompleted(originalHabit.isCompleted());
        history.setDifficulty(originalHabit.getDifficulty());
        history.setDescription(originalHabit.getDescription());
        history.setOriginalHabit(originalHabit);
    }
}
