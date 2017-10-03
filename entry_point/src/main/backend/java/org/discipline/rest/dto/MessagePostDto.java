package org.discipline.rest.dto;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

/**
 * OLTS on 23.09.2017.
 */
@Data
public class MessagePostDto extends ResourceSupport {
    private String message;
}
