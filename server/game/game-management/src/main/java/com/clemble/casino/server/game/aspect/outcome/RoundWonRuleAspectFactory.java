package com.clemble.casino.server.game.aspect.outcome;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.game.lifecycle.management.RoundGameContext;
import com.clemble.casino.game.lifecycle.configuration.RoundGameConfiguration;
import com.clemble.casino.game.lifecycle.management.RoundGameState;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.springframework.core.Ordered;

import com.clemble.casino.game.lifecycle.management.event.GameEndedEvent;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.RoundGameAspectFactory;

/**
 * Created by mavarazy on 23/12/13.
 */
public class RoundWonRuleAspectFactory implements RoundGameAspectFactory<GameEndedEvent<?>> {

    final private SystemNotificationService systemNotificationService;

    public RoundWonRuleAspectFactory(SystemNotificationService systemNotificationService) {
        this.systemNotificationService = checkNotNull(systemNotificationService);
    }

    @Override
    public GameAspect<GameEndedEvent<?>> construct(RoundGameConfiguration configuration, RoundGameState roundState) {
        // Step 1. Checking won rule specified
        if (configuration.getWonRule() == null)
            return null;
        // Step 2. Checking won rule
        switch (configuration.getWonRule()) {
            case price:
                return new RoundWonByPriceRuleAspect(configuration.getPrice(), systemNotificationService);
            case spent:
                return new RoundWonBySpentRuleAspect(configuration.getPrice().getCurrency(), systemNotificationService);
            case owned:
                return new RoundWonByOwnedRuleAspect(configuration.getPrice().getCurrency(), systemNotificationService);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int getOrder(){
        return Ordered.LOWEST_PRECEDENCE - 2;
    }

}
