package com.clemble.casino.server.game.aspect.notification;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import org.springframework.core.Ordered;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.player.PlayerAwareUtils;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;
import com.clemble.casino.server.player.notification.PlayerNotificationService;

public class PlayerNotificationRuleAspectFactory implements GameAspectFactory<GameManagementEvent, GameContext<?>, GameConfiguration> {

    final private PlayerNotificationService notificationService;

    public PlayerNotificationRuleAspectFactory(PlayerNotificationService notificationService) {
        this.notificationService = checkNotNull(notificationService);
    }

    @Override
    public GameAspect<GameManagementEvent> construct(GameConfiguration configuration, GameContext<?> context) {
        switch (configuration.getPrivacyRule()) {
            case everybody:
                return new PublicNotificationRuleAspect(context.getSession(), PlayerAwareUtils.toPlayerList(context.getPlayerContexts()), notificationService);
            case players:
                return new PrivateNotificationRuleAspect(PlayerAwareUtils.toPlayerList(context.getPlayerContexts()), notificationService);
            default:
                throw ClembleCasinoException.fromError(ClembleCasinoError.GameSpecificationInvalid);
        }
    }

    @Override
    public int getOrder(){
        return Ordered.LOWEST_PRECEDENCE - 1;
    }

}
