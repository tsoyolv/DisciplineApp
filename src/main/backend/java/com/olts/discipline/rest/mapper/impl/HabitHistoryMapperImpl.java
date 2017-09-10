package com.olts.discipline.rest.mapper.impl;

import com.olts.discipline.entity.Habit;
import com.olts.discipline.entity.HabitHistory;
import com.olts.discipline.rest.dto.HabitHistoryDto;
import com.olts.discipline.rest.mapper.HabitHistoryMapper;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * OLTS on 10.09.2017.
 */
@Component("habitHistoryMapper")
public class HabitHistoryMapperImpl implements HabitHistoryMapper {
    @Resource
    private EntityLinks entityLinks;

    @Override
    public HabitHistoryDto pojoToDto(HabitHistory history) {
        HabitHistoryDto historyDto = new HabitHistoryDto();
        historyDto.setName(history.getName());
        historyDto.setDescription(history.getDescription());
        historyDto.setCompleted(history.isCompleted());
        historyDto.setCompletedCount(history.getCompletedCount());
        historyDto.setDifficulty(history.getDifficulty());
        historyDto.setWasCompleted(history.getWasCompleted());
        LinkBuilder linkBuilder = entityLinks.linkForSingleResource(Habit.class, history.getOriginalHabit().getId());
        historyDto.add(linkBuilder.withRel("originalHabit"));
        Link link = entityLinks.linkForSingleResource(HabitHistory.class, history.getId()).withSelfRel();
        historyDto.add(link);
        return historyDto;
    }
}
