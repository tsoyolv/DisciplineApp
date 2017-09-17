package com.olts.discipline.api.repository;

import com.olts.discipline.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * OLTS on 11.05.2017.
 */
@RepositoryRestResource
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User findByUsername(String username);

    @Query("select u from User u inner join u.groups g where g.id=:userGroupId")
    Page<User> findByUserGroup(@Param("userGroupId")Long userGroupId, Pageable pageable);
}