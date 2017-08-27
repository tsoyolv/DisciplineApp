package com.olts.discipline.rest.dto;

import lombok.Data;

/**
 * OLTS on 27.08.2017.
 */
@Data
public class LinkDto {
    private String name;
    private String link;

    public LinkDto(String name, String link) {
        this.name = name;
        this.link = link;
    }
}
