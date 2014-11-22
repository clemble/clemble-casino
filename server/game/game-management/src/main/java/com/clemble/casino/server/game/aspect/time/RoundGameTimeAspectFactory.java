package com.clemble.casino.server.game.aspect.time;

import com.clemble.casino.game.lifecycle.configuration.RoundGameConfiguration;
import com.clemble.casino.game.lifecycle.management.RoundGameState;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.springframework.core.Ordered;

import com.clemble.casino.game.lifecycle.management.event.GameManagementEvent;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.RoundGameAspectFactory;

public class RoundGameTimeAspectFactory implements RoundGameAspectFactory<GameManagementEvent> {

    final private SystemNotificationService systemNotificationService;

    public RoundGameTimeAspectFactory(SystemNotificationService systemNotificationService) {
        this.systemNotificationService = systemNotificationService;
    }

    @Override
    public GameAspect<GameManagementEvent> construct(RoundGameConfiguration configuration, RoundGameState roundState) {
        return new RoundGameTimeAspect(configuration, roundState.getContext(), systemNotificationService);
    }

    @Override
    public int getOrder(){
        return HIGHEST_PRECEDENCE;
    }

}
