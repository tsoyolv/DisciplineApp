package com.olts.discipline.api.repository;

import com.olts.discipline.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * OLTS on 11.05.2017.
 */
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User findByUsername(String username);
}