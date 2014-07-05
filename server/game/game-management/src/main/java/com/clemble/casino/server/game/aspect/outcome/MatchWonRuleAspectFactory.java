package com.clemble.casino.server.game.aspect.outcome;

import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.event.server.MatchEndedEvent;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.server.player.presence.SystemNotificationService;
import org.springframework.core.Ordered;

import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.MatchGameAspectFactory;

public class MatchWonRuleAspectFactory implements MatchGameAspectFactory<MatchEndedEvent> {

    final private SystemNotificationService systemNotificationService;

    public MatchWonRuleAspectFactory(SystemNotificationService systemNotificationService) {
        this.systemNotificationService = systemNotificationService;
    }

    @Override
    public GameAspect<MatchEndedEvent> construct(MatchGameConfiguration configuration, MatchGameContext potContext) {
        // Step 1. Check won rule
        if (configuration.getWonRule() != null)
            return new MatchWonRuleAspect(configuration.getPrice().getCurrency(), systemNotificationService);
        // Step 2. Checking values
        return null;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 2;
    }

}
