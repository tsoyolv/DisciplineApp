package org.discipline.rest.mapper;

import org.discipline.entity.Challenge;
import org.discipline.rest.dto.ChallengeDto;
import org.discipline.rest.dto.ChallengePostDto;

/**
 * OLTS on 16.09.2017.
 */
public interface ChallengeMapper extends PojoToDtoMapper<Challenge, ChallengeDto> {
    Challenge challengePostDtoToChallenge(ChallengePostDto postDto);
}
