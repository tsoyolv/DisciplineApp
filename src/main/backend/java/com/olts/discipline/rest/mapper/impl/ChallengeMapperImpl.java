package com.olts.discipline.rest.mapper.impl;

import com.olts.discipline.entity.Challenge;
import com.olts.discipline.entity.User;
import com.olts.discipline.rest.api.ChallengeRestController;
import com.olts.discipline.rest.dto.ChallengeDto;
import com.olts.discipline.rest.dto.ChallengePostDto;
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
        challengeDto.add(ChallengeRestController.linkToUserChallenges(challenge.getId()).withRel("userchallenges"));
        challengeDto.add(entityLinks.linkForSingleResource(User.class, challenge.getCreatedBy().getId()).withRel("createdBy"));
        challengeDto.add(entityLinks.linkForSingleResource(Challenge.class, challenge.getId()).withSelfRel());
        return challengeDto;
    }

    @Override
    public Challenge challengePostDtoToChallenge(ChallengePostDto postDto) {
        Challenge challenge = new Challenge();
        challenge.setName(postDto.getName());
        challenge.setDescription(postDto.getDescription());
        challenge.setChallengeDate(postDto.getChallengeDate());
        challenge.setDifficulty(postDto.getDifficulty());
        return challenge;
    }
}
