package com.disciplineapp.rest.mapper;

import com.disciplineapp.entity.Challenge;
import com.disciplineapp.rest.dto.ChallengeDto;
import com.disciplineapp.rest.dto.ChallengePostDto;

/**
 * OLTS on 16.09.2017.
 */
public interface ChallengeMapper extends PojoToDtoMapper<Challenge, ChallengeDto> {
    Challenge challengePostDtoToChallenge(ChallengePostDto postDto);
}
