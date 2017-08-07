package com.olts.discipline.api.repository;

import com.olts.discipline.model.Habit;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * OLTS on 27.05.2017.
 */
public interface HabitRepository extends PagingAndSortingRepository<Habit, Long> {

    List<Habit> findByHabitUserId(Long userId);

    @Override
    @PreAuthorize("@habitRepository.findOne(#id)?.habitUser?.username == authentication.name")
    void delete(@P("id") Long id);

    @Override
    @PreAuthorize("#habit?.habitUser?.username == authentication.name")
    void delete(@P("habit") Habit habit);
}