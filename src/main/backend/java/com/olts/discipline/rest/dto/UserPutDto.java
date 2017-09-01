package com.olts.discipline.rest.dto;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

/**
 * OLTS on 25.08.2017.
 */
@Data
public class UserPutDto extends ResourceSupport {
    private String username;

    private String firstName;

    private String secondName;

    private String lastName;

    private String email;

    private Boolean isHidden;

    private Date birthDate;

    private String city;

    private String country;
}
