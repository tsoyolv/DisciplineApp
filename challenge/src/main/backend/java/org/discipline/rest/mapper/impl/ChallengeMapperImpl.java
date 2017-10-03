package org.discipline.rest.mapper.impl;

import org.discipline.api.service.UserService;
import org.discipline.entity.Challenge;
import org.discipline.entity.User;
import org.discipline.rest.api.ChallengeRestController;
import org.discipline.rest.dto.ChallengeDto;
import org.discipline.rest.dto.ChallengePostDto;
import org.discipline.rest.mapper.ChallengeMapper;
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

    @Resource
    private UserService userService;

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
        challengeDto.setSphere(challenge.getSphere().name());
        challengeDto.setType(challenge.getType().name());

        challengeDto.setVoteableForCurrentUser(true);
        for (User user : challenge.getVotedUsers()) { // todo stream
            if (user.getId() == userService.getCurrent().getId()) {
                challengeDto.setVoteableForCurrentUser(false);
                break;
            }
        }

        challengeDto.setAcceptableForCurrentUser(true);
        for (User user : challenge.getAcceptedUsers()) { // todo stream
            if (user.getId() == userService.getCurrent().getId()) {
                challengeDto.setAcceptableForCurrentUser(false);
                break;
            }
        }
        // users and groups links
        challengeDto.add(entityLinks.linkForSingleResource(Challenge.class, challenge.getId()).slash("vote").withRel("vote"));
        challengeDto.add(entityLinks.linkForSingleResource(Challenge.class, challenge.getId()).slash("accept").withRel("accept"));
        challengeDto.add(ChallengeRestController.linkToUserChallenges(challenge.getId()).withRel("userchallenges"));
        challengeDto.add(ChallengeRestController.linkToChallengeMessages(challenge.getId()).withRel("messages"));
        challengeDto.add(entityLinks.linkForSingleResource(User.class, challenge.getCreatedBy().getId()).withRel("createdBy"));
        challengeDto.add(ChallengeRestController.linkToChallenge(challenge.getId()).withRel("link"));
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
