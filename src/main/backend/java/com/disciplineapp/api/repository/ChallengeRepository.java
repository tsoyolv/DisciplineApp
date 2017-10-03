package com.disciplineapp.api.repository;

import com.disciplineapp.entity.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * OLTS on 14.09.2017.
 */
@RepositoryRestResource(path = "challenges")
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    Page<Challenge> findByCompleted(Boolean completed, Pageable pageable);
    @Query("select distinct c from Challenge c where c.id in " +
            "(select c.id from Challenge c inner join c.users u where u.id=:userId) or c.forAllUsers=true" )// " or c.id in" +
            //"(select c.id from Challenge c inner join c.groups where c.id=:userId)") //todo
    Page<Challenge> findByUserId(@Param("userId")Long userId, Pageable pageable);
    @Query("select distinct c from Challenge c where c.createdBy.id=:createdById")
    Page<Challenge> findByCreatedById(@Param("createdById")Long createdById, Pageable pageable);
}