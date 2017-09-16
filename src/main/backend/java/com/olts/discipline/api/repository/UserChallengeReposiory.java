package com.olts.discipline.api.repository;

import com.olts.discipline.entity.UserChallenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * OLTS on 16.09.2017.
 */
@RepositoryRestResource(path = "user_challenges")
public interface UserChallengeReposiory extends JpaRepository<UserChallenge, Long> {
    @Query("select c from UserChallenge c where c.completedDate is null")
    Page<UserChallenge> findNonCompleted(Pageable pageable);

    @Query("select c from UserChallenge c where c.completedDate is not null")
    Page<UserChallenge> findCompleted(Pageable pageable);
}