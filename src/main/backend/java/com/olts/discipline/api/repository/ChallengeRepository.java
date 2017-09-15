package com.olts.discipline.api.repository;

import com.olts.discipline.entity.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * OLTS on 14.09.2017.
 */
@RepositoryRestResource(path = "challenges")
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    Page<Challenge> findByCompleted(Boolean completed, Pageable pageable);
}