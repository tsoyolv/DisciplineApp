package com.olts.discipline.rest.hateoas.assembler;

import com.olts.discipline.rest.hateoas.EmbeddedResource;
import com.olts.discipline.rest.hateoas.PageDto;
import com.olts.discipline.rest.hateoas.PageableResource;
import com.olts.discipline.rest.mapper.PojoToDtoMapper;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;
import java.util.stream.Collectors;

/**
 * OLTS on 04.09.2017.
 */

public class PageableResourceAssembler<T> implements ResourceAssembler<Page<T>, PageableResource> {

    private PojoToDtoMapper<T, ? extends ResourceSupport> pojoToDtoMapper;
    private String rootLink;

    public PageableResourceAssembler(PojoToDtoMapper<T, ? extends ResourceSupport> pojoToDtoMapper, String rootLink) {
        this.pojoToDtoMapper = pojoToDtoMapper;
        this.rootLink = rootLink;
    }

    @Override
    public PageableResource toResource(Page<T> page) {
        List<ResourceSupport> items = page.getContent().stream().map(e -> pojoToDtoMapper.pojoToDto(e)).collect(Collectors.toList());
        PageableResource pageableResource = new PageableResource(new EmbeddedResource(items), new PageDto(page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()));
        if (page.getNumber() > 0) {
            pageableResource.add(getPrevLink(page));
        }
        if (page.getNumber() < page.getTotalPages() - 1) {
            pageableResource.add(getNextLink(page));
        }
        pageableResource.add(getFirstLink(page));
        pageableResource.add(getLastLink(page));
        pageableResource.add(new Link(rootLink + "{&sort}").withSelfRel());
        return pageableResource;
    }

    private Link getLastLink(Page<T> page) {
       return new Link(rootLink + String.format("?page=%d&size=%d", page.getTotalPages() - 1, page.getSize())).withRel(Link.REL_LAST);
    }

    private Link getFirstLink(Page<T> page) {
        return new Link(rootLink + String.format("?page=%d&size=%d", 0, page.getSize())).withRel(Link.REL_FIRST);
    }

    private Link getNextLink(Page<T> page) {
        return new Link(rootLink + String.format("?page=%d&size=%d", page.getNumber() + 1, page.getSize())).withRel(Link.REL_NEXT);
    }

    private Link getPrevLink(Page<T> page) {
        return new Link(rootLink + String.format("?page=%d&size=%d", page.getNumber() - 1, page.getSize())).withRel(Link.REL_PREVIOUS);
    }
}
