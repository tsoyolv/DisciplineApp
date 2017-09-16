package com.olts.discipline.api.repository;

import com.olts.discipline.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * OLTS on 16.09.2017.
 */
@RepositoryRestResource(path = "groups")
public interface GroupRepository extends JpaRepository<Group, Long> {
}