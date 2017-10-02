package org.discipline.rest.dto;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

/**
 * OLTS on 23.09.2017.
 */

@Data
public class MessageDto extends ResourceSupport {
    private String message;

    private Date wasSent;

    private String username;
}
