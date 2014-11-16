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
import com.clemble.casino.server.player.notification.PlayerNotificationService;

public class GamePlayerNotificationRuleAspectFactory implements GenericGameAspectFactory<GameManagementEvent> {

    final private PlayerNotificationService notificationService;

    public GamePlayerNotificationRuleAspectFactory(PlayerNotificationService notificationService) {
        this.notificationService = checkNotNull(notificationService);
    }

    @Override
    public GameAspect<GameManagementEvent> construct(GameConfiguration configuration, GameState state) {
        switch (configuration.getPrivacyRule()) {
            case world:
                return new GamePublicNotificationRuleAspect(state.getContext().getSessionKey(), PlayerAwareUtils.toPlayerList(state.getContext().getPlayerContexts()), notificationService);
            case me:
                return new GamePrivateNotificationRuleAspect(PlayerAwareUtils.toPlayerList(state.getContext().getPlayerContexts()), notificationService);
            default:
                throw ClembleCasinoException.withKey(ClembleCasinoError.GameSpecificationInvalid, state.getContext().getSessionKey());
        }
    }

    @Override
    public int getOrder(){
        return Ordered.LOWEST_PRECEDENCE - 1;
    }

}
