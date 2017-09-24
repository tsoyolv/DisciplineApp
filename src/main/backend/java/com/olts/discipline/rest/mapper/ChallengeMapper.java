package com.olts.discipline.rest.mapper;

import com.olts.discipline.entity.Challenge;
import com.olts.discipline.rest.dto.ChallengeDto;
import com.olts.discipline.rest.dto.ChallengePostDto;

/**
 * OLTS on 16.09.2017.
 */
public interface ChallengeMapper extends PojoToDtoMapper<Challenge, ChallengeDto> {
    Challenge challengePostDtoToChallenge(ChallengePostDto postDto);
}
