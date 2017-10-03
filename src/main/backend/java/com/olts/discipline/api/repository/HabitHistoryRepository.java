package com.olts.discipline.api.repository;

import com.olts.discipline.entity.habit.HabitHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * OLTS on 20.08.2017.
 */
@RepositoryRestResource(path = "habit/histories")
public interface HabitHistoryRepository extends PagingAndSortingRepository<HabitHistory, Long> {
    Page<HabitHistory> findByOriginalHabitId(Long originalHabitId, Pageable pageable);
}