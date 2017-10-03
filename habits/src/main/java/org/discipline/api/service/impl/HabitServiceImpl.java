package org.discipline.api.service.impl;

import com.olts.discipline.api.repository.HabitRepository;
import com.olts.discipline.api.service.HabitHistoryService;
import com.olts.discipline.api.service.HabitService;
import com.olts.discipline.entity.habit.Habit;
import com.olts.discipline.logic.calculator.UserScoreCalculator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private UserScoreCalculator calculator;

    @Override
    public Habit get(Long id) {
        return repository.findOne(id);
    }

    @Override
    public Page<Habit> getByUserId(Long userId, Boolean achieved, Boolean completed, Integer page, Integer pageSize) {
        PageRequest pageable = new PageRequest(page, pageSize, Sort.Direction.DESC, "createdWhen");
        return repository.findByHabitUserId(userId, achieved, completed, pageable);
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
        completed.setCompleted(true);
        calculator.calculate(completed);
        repository.save(completed);
        historyService.create(completed);
    }
}