package com.disciplineapp.api.repository;

import com.disciplineapp.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * OLTS on 16.09.2017.
 */
@RepositoryRestResource(path = "groups")
public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query("select g from Groups g inner join g.users u where u.id=:userId")
    Page<Group> findByUserId(@Param("userId")Long userId, Pageable pageable);
}