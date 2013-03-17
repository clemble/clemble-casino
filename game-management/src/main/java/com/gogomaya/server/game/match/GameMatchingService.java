package com.gogomaya.server.game.match;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.gogomaya.server.game.rule.GameRuleSpecification;
import com.gogomaya.server.game.rule.GameRuleSpecificationFormat;
import com.gogomaya.server.game.rule.participant.FixedParticipantRule;
import com.gogomaya.server.game.rule.participant.LimitedParticipantRule;
import com.gogomaya.server.game.rule.participant.ParticipantRule;
import com.gogomaya.server.game.session.GameSession;
import com.gogomaya.server.game.session.GameSessionRepository;
import com.gogomaya.server.game.session.GameSessionState;

public class GameMatchingService {

    final private RedisTemplate<byte[], Long> redisTemplate;

    final private GameSessionRepository sessionRepository;

    @Inject
    public GameMatchingService(final RedisTemplate<byte[], Long> redisTemplate, final GameSessionRepository sessionRepository) {
        this.redisTemplate = checkNotNull(redisTemplate);
        this.sessionRepository = checkNotNull(sessionRepository);
    }

    public GameSession create(final long player, final GameRuleSpecification ruleSpecification) {
        GameSession resultSession = new GameSession();
        // Step 1. Generate GameRuleSpecification for key and trying to fetch first session
        BoundSetOperations<byte[], Long> boundSetOperations = redisTemplate.boundSetOps(GameRuleSpecificationFormat.toByteArray(ruleSpecification));
        Long sessionId = boundSetOperations.pop();
        if (sessionId != null) {
            // Step 2. Link to the existing session
            resultSession = sessionRepository.findOne(sessionId);
            resultSession.addPlayer(player);
            // Step 2.1. Processing ParticipantRule
            ParticipantRule participantRule = ruleSpecification.getParticipationRule();
            if (participantRule instanceof FixedParticipantRule) {
                FixedParticipantRule fixedParticipantRule = (FixedParticipantRule) participantRule;
                if (fixedParticipantRule.getNumberOfParticipants() == resultSession.getPlayers().size()) {
                    resultSession = switchToActive(resultSession);
                }
            } else if (participantRule instanceof LimitedParticipantRule) {
                LimitedParticipantRule limitedParticipantRule = (LimitedParticipantRule) participantRule;
                if (limitedParticipantRule.getMinPlayers() <= resultSession.getPlayers().size()) {
                    resultSession = switchToActive(resultSession);
                }
            }
        } else {
            // Step 3. Create new session with the player
            resultSession = new GameSession();
            resultSession.setGameRuleSpecification(ruleSpecification);
            resultSession.addPlayer(player);
            // Step 3.1. Saving GameSession in session repository
            resultSession = sessionRepository.saveAndFlush(resultSession);
        }

        // Step 4. Adding saved session id to the Redis Set
        if (resultSession.getSessionState() == GameSessionState.Inactive)
            boundSetOperations.add(resultSession.getSessionId());

        return resultSession;
    }

    private GameSession switchToActive(GameSession gameSession) {
        gameSession.setSessionState(GameSessionState.Active);
        sessionRepository.saveAndFlush(gameSession);
        return gameSession;
    }

}
