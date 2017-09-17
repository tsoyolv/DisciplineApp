package com.olts.discipline.rest.dto;

import lombok.Data;

import java.util.Date;

/**
 * OLTS on 17.09.2017.
 */
@Data
public class ChallengePostDto {
    private String name;

    private int difficulty;

    private String description;

    private Date challengeDate;

    private boolean withCreator;
}
