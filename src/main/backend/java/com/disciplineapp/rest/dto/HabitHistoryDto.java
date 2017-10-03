package com.disciplineapp.rest.dto;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

/**
 * OLTS on 10.09.2017.
 */
@Data
public class HabitHistoryDto extends ResourceSupport {
    private String name;

    private int difficulty;

    private boolean isCompleted;

    private String description;

    private Date completedDate;

    private int completedCount;
}
