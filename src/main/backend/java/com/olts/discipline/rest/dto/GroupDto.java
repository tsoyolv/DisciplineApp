package com.olts.discipline.rest.dto;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

/**
 * OLTS on 16.09.2017.
 */

@Data
public class GroupDto extends ResourceSupport {
    private String name;

    private String description;

    private Date createdWhen;

    private Date updatedWhen;
}

