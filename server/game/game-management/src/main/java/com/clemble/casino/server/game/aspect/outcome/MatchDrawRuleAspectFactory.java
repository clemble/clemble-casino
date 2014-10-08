package com.clemble.casino.server.game.aspect.outcome;

import com.clemble.casino.game.lifecycle.management.MatchGameContext;
import com.clemble.casino.game.lifecycle.management.event.MatchEndedEvent;
import com.clemble.casino.game.lifecycle.configuration.MatchGameConfiguration;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.springframework.core.Ordered;

import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.MatchGameAspectFactory;

public class MatchDrawRuleAspectFactory implements MatchGameAspectFactory<MatchEndedEvent> {

    final private SystemNotificationService systemNotificationService;

    public MatchDrawRuleAspectFactory(SystemNotificationService systemNotificationService) {
        this.systemNotificationService = systemNotificationService;
    }

    @Override
    public GameAspect<MatchEndedEvent> construct(MatchGameConfiguration configuration, MatchGameContext potContext) {
        // Step 1. Check won rule
        if (configuration.getDrawRule() != null)
            return new MatchDrawRuleAspect(configuration.getPrice().getCurrency(), systemNotificationService);
        // Step 2. Checking values
        return null;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 2;
    }

}
