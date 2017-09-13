package com.olts.discipline.rest.mapper;

import org.springframework.hateoas.ResourceSupport;

/**
 * OLTS on 04.09.2017.
 */
public interface PojoToDtoMapper<IN, OUT extends ResourceSupport> {
    OUT pojoToDto(IN object);
}
