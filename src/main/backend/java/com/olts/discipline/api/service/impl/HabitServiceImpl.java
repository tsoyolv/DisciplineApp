package com.olts.discipline.api.service.impl;

import com.olts.discipline.api.repository.HabitRepository;
import com.olts.discipline.api.service.HabitHistoryService;
import com.olts.discipline.api.service.HabitService;
import com.olts.discipline.api.service.UserService;
import com.olts.discipline.model.Habit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * OLTS on 20.08.2017.
 */
@Service
public class HabitServiceImpl implements HabitService {
    @Resource
    private HabitRepository repository;
    @Resource
    private HabitHistoryService historyRepository;
    @Resource
    private UserService userService;

    @Override
    public Habit get(long id) {
        return repository.findOne(id);
    }

    @Override
    public Collection<Habit> getNotCompletedUserHabits(long userId) {
        return userService.getUserHabits(userId);
    }

    @Override
    public void complete(long id) {
        Habit completed = repository.findOne(id);
        userService.recalculateScores(completed.getHabitUser().getId(), completed);
        completed.setCompleted(true);
        completed.setCount(completed.getCount() + 1);
        repository.save(completed);
        historyRepository.create(completed);
    }
}
