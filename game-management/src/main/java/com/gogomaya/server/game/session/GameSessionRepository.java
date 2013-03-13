package com.gogomaya.server.game.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.gogomaya.server.game.rule.GameRuleSpecification;

@Repository
@Component
public interface GameSessionRepository extends JpaRepository<GameSession, Long> {

    public GameSession findByRuleSpecification(GameRuleSpecification ruleSpecification);

    public GameSession findBySessionStateAndRuleSpecification(GameRuleSpecification ruleSpecification, GameSessionState sessionState);

}
