package org.discipline.api.repository;

import org.discipline.entity.habit.Habit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * OLTS on 27.05.2017.
 */
@RepositoryRestResource(path = "habits")
public interface HabitRepository extends PagingAndSortingRepository<Habit, Long> {

    @Query("select h from Habit h where h.habitUser.id=:userId and h.achieved=:achieved and h.completed=:completed")
    Page<Habit> findByHabitUserId(@Param("userId") Long userId, @Param("achieved") Boolean achieved, @Param("completed") Boolean completed, Pageable pageable);

    @Query("select h from Habit h where h.achieved=:achieved and h.completed=:completed")
    Page<Habit> findAllCompletedAchieved(@Param("achieved") Boolean achieved, @Param("completed") Boolean completed, Pageable pageable);

    @Override
    @PreAuthorize("@habitRepository.findOne(#id)?.habitUser?.username == authentication.name")
    void delete(@P("id") Long id);

    @Override
    @PreAuthorize("#habit?.habitUser?.username == authentication.name")
    void delete(@P("habit") Habit habit);
}