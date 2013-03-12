package com.gogomaya.server.game.session;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gogomaya.server.game.rule.GameRuleSpecification;

public interface GameSessionRepository extends JpaRepository<GameSession, String> {

    public GameSession findByRuleSpecification(GameRuleSpecification ruleSpecification);

    public GameSession findBySessionStateAndRuleSpecification(GameRuleSpecification ruleSpecification, GameSessionState sessionState);

}
