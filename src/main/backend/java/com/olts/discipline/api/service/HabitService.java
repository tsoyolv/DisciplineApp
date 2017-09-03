package com.olts.discipline.api.service;

import com.olts.discipline.entity.Habit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * OLTS on 20.08.2017.
 */
public interface HabitService {
    Habit get(Long id);

    List<Habit> getByUserId(Long userId, Boolean achieved, Boolean completed, Integer page, Integer pageSize);

    Page<Habit> get(Boolean achieved, Boolean completed, Pageable pageable);

    Habit update(Habit habit);

    void complete(Long id);
}
