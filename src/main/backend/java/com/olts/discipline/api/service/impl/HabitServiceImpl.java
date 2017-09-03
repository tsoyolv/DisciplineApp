package com.olts.discipline.api.service.impl;

import com.olts.discipline.api.repository.HabitRepository;
import com.olts.discipline.api.service.HabitHistoryService;
import com.olts.discipline.api.service.HabitService;
import com.olts.discipline.api.service.UserScoreRecalculationService;
import com.olts.discipline.entity.Habit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
    public Habit get(Long id) {
        return repository.findOne(id);
    }

    @Override
    public List<Habit> getByUserId(Long userId, Boolean achieved, Boolean completed, Integer page, Integer pageSize) {
        return repository.findByHabitUserId(userId, achieved, completed, new PageRequest(page, pageSize)).getContent();
    }

    @Override
    public Page<Habit> get(Boolean achieved, Boolean completed, Pageable pageable) {
        return repository.findAllCompletedAchieved(achieved, completed, pageable);
    }


    @Override
    public Habit update(Habit habit) {
        return repository.save(habit);
    }

    @Override
    public void complete(Long id) {
        Habit completed = repository.findOne(id);
        recalculationService.recalculateScores(completed.getHabitUser().getId(), completed);
        completed.setCompleted(true);
        completed.setCompletedCount(completed.getCompletedCount() + 1);
        repository.save(completed);
        historyService.create(completed);
    }
}
