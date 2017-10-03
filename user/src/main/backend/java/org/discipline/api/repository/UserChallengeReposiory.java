package org.discipline.api.repository;

import org.discipline.entity.UserChallenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * OLTS on 16.09.2017.
 */
@RepositoryRestResource(path = "user_challenges")
public interface UserChallengeReposiory extends JpaRepository<UserChallenge, Long> {
    @Query("select c from UserChallenge c where c.originalChallenge.id=:origChallenge and c.completedDate is null")
    Page<UserChallenge> findByOriginalChallengeNonCompleted(@Param("origChallenge") Long origChallenge, Pageable pageable);

    @Query("select c from UserChallenge c where c.originalChallenge.id=:origChallenge and c.completedDate is not null")
    Page<UserChallenge> findByOriginalChallengeCompleted(@Param("origChallenge") Long origChallenge, Pageable pageable);

    @Query("select c from UserChallenge c where c.challengeUser.id=:userId and c.completedDate is null")
    Page<UserChallenge> findByUserNonCompleted(@Param("userId") Long userId, Pageable pageable);

    @Query("select c from UserChallenge c where c.challengeUser.id=:userId and c.completedDate is not null")
    Page<UserChallenge> findByUserCompleted(@Param("userId") Long userId, Pageable pageable);
}