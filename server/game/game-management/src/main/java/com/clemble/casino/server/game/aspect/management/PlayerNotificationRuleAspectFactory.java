package com.clemble.casino.server.game.aspect.management;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;
import com.clemble.casino.server.player.notification.PlayerNotificationService;

public class PlayerNotificationRuleAspectFactory implements GameAspectFactory<GameManagementEvent> {

    final private PlayerNotificationService notificationService;

    public PlayerNotificationRuleAspectFactory(PlayerNotificationService notificationService) {
        this.notificationService = checkNotNull(notificationService);
    }

    @Override
    public GameAspect<GameManagementEvent> construct(GameInitiation initiation, GameContext construction) {
        switch (initiation.getSpecification().getPrivacyRule()) {
            case everybody:
                return new PublicNotificationRuleAspect(initiation, notificationService);
            case players:
                return new PrivateNotificationRuleAspect(initiation, notificationService);
            default:
                throw ClembleCasinoException.fromError(ClembleCasinoError.GameSpecificationInvalid);
        }
    }


}
