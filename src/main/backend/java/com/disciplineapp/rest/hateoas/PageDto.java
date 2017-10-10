package com.disciplineapp.rest.hateoas;

import lombok.Data;

/**
 * OLTS on 03.09.2017.
 */
@Data
public class PageDto {
    private Integer size;
    private Integer number;
    private Long totalElements;
    private Integer totalPages;

    public PageDto(Integer size, Integer number, Long totalElements, Integer totalPages) {
        this.size = size;
        this.number = number;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }
}