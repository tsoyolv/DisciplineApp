package com.olts.discipline.api.repository;

import com.olts.discipline.model.Habit;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * OLTS on 27.05.2017.
 */
public interface HabitRepository extends PagingAndSortingRepository<Habit, Long> {
}
