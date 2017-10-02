package org.discipline.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

/**
 * OLTS on 24.09.2017.
 */
public class EnumValuesDto extends ResourceSupport {
    private final List<String> values;

    @JsonCreator
    public EnumValuesDto(@JsonProperty("values")List<String> values) {
        this.values = values;
    }

    public List<String> getItems() {
        return values;
    }
}