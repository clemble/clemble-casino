package com.clemble.casino.server.game.aspect.outcome;

import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.springframework.core.Ordered;

import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.game.configuration.GameConfiguration;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;

/**
 * Created by mavarazy on 23/12/13.
 */
public class RoundDrawRuleAspectFactory implements GameAspectFactory<GameEndedEvent<?>, GameContext<?>, GameConfiguration> {

    final private SystemNotificationService systemNotificationService;

    public RoundDrawRuleAspectFactory(SystemNotificationService systemNotificationService) {
        this.systemNotificationService = systemNotificationService;
    }

    @Override
    public GameAspect<GameEndedEvent<?>> construct(GameConfiguration configuration, GameContext<?> context) {
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
