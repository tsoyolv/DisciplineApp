package com.disciplineapp.rest.hateoas;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

/**
 * OLTS on 03.09.2017.
 */
public class PageableResource extends ResourceSupport {

    @JsonProperty("_embedded")
    private final EmbeddedResource embedded;
    @JsonProperty("page")
    private final PageDto page;

    @JsonCreator
    public PageableResource(EmbeddedResource embedded, PageDto page) {
        this.embedded = embedded;
        this.page = page;
    }

    public EmbeddedResource getEmbedded() {
        return embedded;
    }

    public PageDto getPage() {
        return page;
    }


}