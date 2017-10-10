package com.disciplineapp.rest.mapper.impl;

import com.disciplineapp.entity.habit.HabitHistory;
import com.disciplineapp.rest.mapper.HabitHistoryMapper;
import com.disciplineapp.entity.habit.Habit;
import com.disciplineapp.rest.dto.HabitHistoryDto;
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
        historyDto.setCompletedDate(history.getCompletedDate());
        LinkBuilder linkBuilder = entityLinks.linkForSingleResource(Habit.class, history.getOriginalHabit().getId());
        historyDto.add(linkBuilder.withRel("originalHabit"));
        Link link = entityLinks.linkForSingleResource(HabitHistory.class, history.getId()).withSelfRel();
        historyDto.add(link);
        return historyDto;
    }
}
