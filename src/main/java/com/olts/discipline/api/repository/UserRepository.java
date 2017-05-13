package com.olts.discipline.api.repository;

import com.olts.discipline.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * OLTS on 11.05.2017.
 */
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}