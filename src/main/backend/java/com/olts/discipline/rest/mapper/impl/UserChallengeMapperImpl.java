package com.olts.discipline.rest.mapper.impl;

import com.olts.discipline.entity.Challenge;
import com.olts.discipline.entity.User;
import com.olts.discipline.entity.UserChallenge;
import com.olts.discipline.rest.dto.UserChallengeDto;
import com.olts.discipline.rest.mapper.UserChallengeMapper;
import org.springframework.hateoas.EntityLinks;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * OLTS on 16.09.2017.
 */
@Component
public class UserChallengeMapperImpl implements UserChallengeMapper {

    @Resource
    private EntityLinks entityLinks;

    @Override
    public UserChallengeDto pojoToDto(UserChallenge obj) {
        UserChallengeDto dto = new UserChallengeDto();
        dto.setVotes(obj.getVotes());
        dto.setAcceptedDate(obj.getAcceptedDate());
        dto.setCompletedDate(obj.getCompletedDate());
        dto.setName(obj.getOriginalChallenge().getName());
        dto.setUpdatedWhen(obj.getUpdatedWhen());
        dto.setDifficulty(obj.getOriginalChallenge().getDifficulty());
        dto.setDescription(obj.getOriginalChallenge().getDescription());
        dto.setChallengeDate(obj.getOriginalChallenge().getChallengeDate());
        dto.setWithCreator(obj.getOriginalChallenge().isWithCreator());
        dto.add(entityLinks.linkForSingleResource(User.class, obj.getChallengeUser().getId()).withRel("challengeUser"));
        dto.add(entityLinks.linkForSingleResource(Challenge.class, obj.getOriginalChallenge().getId()).withRel("originalChallenge"));
        dto.add(entityLinks.linkForSingleResource(UserChallenge.class, obj.getId()).withSelfRel());
        return dto;
    }
}
