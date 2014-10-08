package com.clemble.casino.server.game.aspect.notification;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.game.lifecycle.management.GameState;
import org.springframework.core.Ordered;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.lifecycle.management.GameContext;
import com.clemble.casino.game.lifecycle.management.event.GameManagementEvent;
import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;
import com.clemble.casino.player.PlayerAwareUtils;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;
import com.clemble.casino.server.player.notification.PlayerNotificationService;

public class PlayerNotificationRuleAspectFactory implements GameAspectFactory<GameManagementEvent, GameState<?, ?>, GameConfiguration> {

    final private PlayerNotificationService notificationService;

    public PlayerNotificationRuleAspectFactory(PlayerNotificationService notificationService) {
        this.notificationService = checkNotNull(notificationService);
    }

    @Override
    public GameAspect<GameManagementEvent> construct(GameConfiguration configuration, GameState<?, ?> state) {
        switch (configuration.getPrivacyRule()) {
            case everybody:
                return new PublicNotificationRuleAspect(state.getContext().getSessionKey(), PlayerAwareUtils.toPlayerList(state.getContext().getPlayerContexts()), notificationService);
            case players:
                return new PrivateNotificationRuleAspect(PlayerAwareUtils.toPlayerList(state.getContext().getPlayerContexts()), notificationService);
            default:
                throw ClembleCasinoException.withKey(ClembleCasinoError.GameSpecificationInvalid, state.getContext().getSessionKey());
        }
    }

    @Override
    public int getOrder(){
        return Ordered.LOWEST_PRECEDENCE - 1;
    }

}
