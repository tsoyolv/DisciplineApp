package com.disciplineapp.rest.hateoas.assembler;

import com.disciplineapp.rest.hateoas.EmbeddedResource;
import com.disciplineapp.rest.hateoas.PageDto;
import com.disciplineapp.rest.hateoas.PageableResource;
import com.disciplineapp.rest.mapper.PojoToDtoMapper;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * OLTS on 04.09.2017.
 */

public class PageableResourceAssembler<T> implements ResourceAssembler<Page<T>, PageableResource> {

    private PojoToDtoMapper<T, ? extends ResourceSupport> pojoToDtoMapper;
    private String rootLink;
    private Map<String, String> params;

    public PageableResourceAssembler(PojoToDtoMapper<T, ? extends ResourceSupport> pojoToDtoMapper, String rootLink, Map<String, String> params) {
        this.pojoToDtoMapper = pojoToDtoMapper;
        this.rootLink = rootLink;
        this.params = params;
    }

    @Override
    public PageableResource toResource(Page<T> page) {
        if (page.getTotalPages() == 0) {
            return null;
        }
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
       return new Link(rootLink + String.format(getParamsString(), page.getTotalPages() - 1, page.getSize())).withRel(Link.REL_LAST);
    }

    private String getParamsString() {
        StringBuilder paramStr = new StringBuilder("?page=%d&size=%d");
        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                paramStr.append("&").append(param.getKey()).append("=").append(param.getValue());
            }
        }
        return paramStr.toString();
    }

    private Link getFirstLink(Page<T> page) {
        return new Link(rootLink + String.format(getParamsString(), 0, page.getSize())).withRel(Link.REL_FIRST);
    }

    private Link getNextLink(Page<T> page) {
        return new Link(rootLink + String.format(getParamsString(), page.getNumber() + 1, page.getSize())).withRel(Link.REL_NEXT);
    }

    private Link getPrevLink(Page<T> page) {
        return new Link(rootLink + String.format(getParamsString(), page.getNumber() - 1, page.getSize())).withRel(Link.REL_PREVIOUS);
    }
}
