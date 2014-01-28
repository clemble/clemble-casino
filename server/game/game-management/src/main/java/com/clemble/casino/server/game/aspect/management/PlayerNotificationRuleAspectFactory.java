package com.clemble.casino.server.game.aspect.management;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import org.springframework.core.Ordered;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.construct.ServerGameInitiation;
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
    public GameAspect<GameManagementEvent> construct(ServerGameInitiation initiation) {
        switch (initiation.getSpecification().getPrivacyRule()) {
            case everybody:
                return new PublicNotificationRuleAspect(initiation.getSession(), initiation.getParticipants(), notificationService);
            case players:
                return new PrivateNotificationRuleAspect(initiation.getParticipants(), notificationService);
            default:
                throw ClembleCasinoException.fromError(ClembleCasinoError.GameSpecificationInvalid);
        }
    }

    @Override
    public int getOrder(){
        return Ordered.LOWEST_PRECEDENCE - 1;
    }

}
