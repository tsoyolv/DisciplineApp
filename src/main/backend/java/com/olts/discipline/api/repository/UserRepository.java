package com.olts.discipline.api.repository;

import com.olts.discipline.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * OLTS on 11.05.2017.
 */
@RepositoryRestResource
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User findByUsername(String username);
}