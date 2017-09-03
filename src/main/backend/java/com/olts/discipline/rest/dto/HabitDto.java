package com.olts.discipline.rest.dto;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

/**
 * OLTS on 03.09.2017.
 */
@Data
public class HabitDto extends ResourceSupport {

    private String name;

    private int difficulty;

    private boolean isCompleted;

    private String description;

    private boolean achieved;

    private Date createdWhen;

    private Date updatedWhen;

    private int completedCount;
}
