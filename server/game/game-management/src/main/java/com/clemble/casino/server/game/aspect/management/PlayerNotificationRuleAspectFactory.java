package com.clemble.casino.server.game.aspect.management;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import org.springframework.core.Ordered;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.player.PlayerAwareUtils;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.MatchGameAspectFactory;
import com.clemble.casino.server.player.notification.PlayerNotificationService;

public class PlayerNotificationRuleAspectFactory implements MatchGameAspectFactory<GameManagementEvent> {

    final private PlayerNotificationService notificationService;

    public PlayerNotificationRuleAspectFactory(PlayerNotificationService notificationService) {
        this.notificationService = checkNotNull(notificationService);
    }

    @Override
    public GameAspect<GameManagementEvent> construct(MatchGameConfiguration configuration, MatchGameContext context) {
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
