package com.olts.discipline.rest.dto;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

/**
 * OLTS on 16.09.2017.
 */
@Data
public class UserChallengeDto extends ResourceSupport {
    private int votes;

    private Date completedDate;

    private Date acceptedDate;

    private Date updatedWhen;

    /* Challenge properties */

    private String name;

    private int difficulty;

    private String description;

    private Date challengeDate;

    private boolean withCreator;

    private boolean voteableForCurrentUser;
}
