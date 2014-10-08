package com.clemble.casino.server.game.aspect.outcome;

import com.clemble.casino.game.lifecycle.configuration.RoundGameConfiguration;
import com.clemble.casino.game.lifecycle.management.RoundGameState;
import com.clemble.casino.game.lifecycle.management.event.RoundEndedEvent;
import com.clemble.casino.server.game.aspect.RoundGameAspectFactory;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.springframework.core.Ordered;

import com.clemble.casino.game.lifecycle.management.event.GameEndedEvent;
import com.clemble.casino.server.game.aspect.GameAspect;

/**
 * Created by mavarazy on 23/12/13.
 */
public class RoundDrawRuleAspectFactory implements RoundGameAspectFactory<RoundEndedEvent<?>> {

    final private SystemNotificationService systemNotificationService;

    public RoundDrawRuleAspectFactory(SystemNotificationService systemNotificationService) {
        this.systemNotificationService = systemNotificationService;
    }

    @Override
    public GameAspect<RoundEndedEvent<?>> construct(RoundGameConfiguration configuration, RoundGameState state) {
        // Step 1. Checking draw rule
        if (configuration.getDrawRule() == null)
            return null;
        // Step 2. Constructing draw rule
        switch (configuration.getDrawRule()) {
            case owned:
                return new RoundDrawByOwnedRuleAspect(configuration.getPrice().getCurrency(), systemNotificationService);
            case spent:
                return RoundDrawBySpentRuleAspect.INSTANCE;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int getOrder(){
        return Ordered.LOWEST_PRECEDENCE - 2;
    }

}
