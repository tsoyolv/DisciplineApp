package com.olts.discipline.api.repository;

import com.olts.discipline.model.HabitHistory;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * OLTS on 20.08.2017.
 */
@RepositoryRestResource(exported = false)
public interface HabitHistoryRepository extends PagingAndSortingRepository<HabitHistory, Long> {

}
