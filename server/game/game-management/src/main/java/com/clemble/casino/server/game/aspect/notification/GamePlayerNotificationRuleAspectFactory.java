package com.clemble.casino.server.game.aspect.notification;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;
import com.clemble.casino.game.lifecycle.management.GameState;
import com.clemble.casino.game.lifecycle.management.event.GameManagementEvent;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GenericGameAspectFactory;
import org.springframework.core.Ordered;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.player.PlayerAwareUtils;
import com.clemble.casino.server.player.notification.ServerNotificationService;

import java.util.List;

public class GamePlayerNotificationRuleAspectFactory implements GenericGameAspectFactory<GameManagementEvent> {

    final private ServerNotificationService notificationService;

    public GamePlayerNotificationRuleAspectFactory(ServerNotificationService notificationService) {
        this.notificationService = checkNotNull(notificationService);
    }

    @Override
    public GameAspect<GameManagementEvent> construct(GameConfiguration configuration, GameState state) {
        String sessionKey = state.getContext().getSessionKey();
        List<String> players = PlayerAwareUtils.toPlayerList(state.getContext().getPlayerContexts());
        return new GamePublicNotificationRuleAspect(sessionKey, players, notificationService);
    }

    @Override
    public int getOrder(){
        return Ordered.LOWEST_PRECEDENCE - 1;
    }

}
