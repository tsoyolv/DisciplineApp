package com.olts.discipline.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

/**
 * OLTS on 03.09.2017.
 */
public class EmbeddedResource extends ResourceSupport {
    private final List<? extends ResourceSupport> items;

    @JsonCreator
    public EmbeddedResource(@JsonProperty("items")List<? extends ResourceSupport> items) {
        this.items = items;
    }

    public List<? extends ResourceSupport> getItems() {
        return items;
    }
}