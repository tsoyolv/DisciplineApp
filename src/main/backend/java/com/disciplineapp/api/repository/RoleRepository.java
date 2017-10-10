package com.disciplineapp.api.repository;

import com.disciplineapp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * OLTS on 13.05.2017.
 */
@RepositoryRestResource(exported = false)
public interface RoleRepository extends JpaRepository<Role, Long> {
}

