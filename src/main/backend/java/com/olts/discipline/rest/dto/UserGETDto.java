package com.olts.discipline.rest.dto;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

/**
 * OLTS on 25.08.2017.
 */
@Data
public class UserGETDto extends ResourceSupport {

    private Date createdWhen;

    private String username;

    private String firstName;

    private String secondName;

    private String lastName;

    private String email;

    private Integer taskScore;

    private Integer habitScore;

    private Integer score;

    private Boolean isHidden;

    private Integer level;

    private Integer levelPercentage;

    private Integer progressPerDay;

    private Integer rank;

    private Date birthDate;

    private String city;

    private String country;
}
