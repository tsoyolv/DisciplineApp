package com.olts.discipline.rest.mapper.impl;

import com.olts.discipline.entity.Challenge;
import com.olts.discipline.entity.User;
import com.olts.discipline.rest.dto.ChallengeDto;
import com.olts.discipline.rest.mapper.ChallengeMapper;
import org.springframework.hateoas.EntityLinks;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * OLTS on 16.09.2017.
 */
@Component
public class ChallengeMapperImpl implements ChallengeMapper {

    @Resource
    private EntityLinks entityLinks;

    @Override
    public ChallengeDto pojoToDto(Challenge challenge) {
        ChallengeDto challengeDto = new ChallengeDto();
        challengeDto.setName(challenge.getName());
        challengeDto.setDifficulty(challenge.getDifficulty());
        challengeDto.setDescription(challenge.getDescription());
        challengeDto.setChallengeDate(challenge.getChallengeDate());
        challengeDto.setVotes(challenge.getVotes());
        challengeDto.setWithCreator(challenge.isWithCreator());
        challengeDto.setAcceptedCount(challenge.getAcceptedCount());
        challengeDto.setCompletedCount(challenge.getAcceptedCount());
        challengeDto.setCreatedWhen(challenge.getCreatedWhen());
        challengeDto.setUpdatedWhen(challenge.getUpdatedWhen());
        // users and groups
        challengeDto.add(entityLinks.linkForSingleResource(User.class, challenge.getCreatedBy().getId()).withRel("createdBy"));
        challengeDto.add(entityLinks.linkForSingleResource(Challenge.class, challenge.getId()).withSelfRel());
        return challengeDto;
    }
}
