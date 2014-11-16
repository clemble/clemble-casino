package com.clemble.casino.server.aspect.notification;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.event.ManagementEvent;
import com.clemble.casino.lifecycle.configuration.Configuration;
import com.clemble.casino.lifecycle.management.State;
import com.clemble.casino.server.aspect.ClembleAspect;
import com.clemble.casino.server.aspect.ClembleAspectFactory;
import com.clemble.casino.server.aspect.GenericClembleAspectFactory;
import org.springframework.core.Ordered;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.player.PlayerAwareUtils;
import com.clemble.casino.server.player.notification.PlayerNotificationService;

import java.util.function.Function;

public class PlayerNotificationRuleAspectFactory<S extends State> implements GenericClembleAspectFactory<ManagementEvent, Configuration, S> {

    final private Function<S, String> stateToKey;
    final private PlayerNotificationService notificationService;

    public PlayerNotificationRuleAspectFactory(
        PlayerNotificationService notificationService,
        Function<S, String> stateToKey) {
        this.stateToKey = checkNotNull(stateToKey);
        this.notificationService = checkNotNull(notificationService);
    }

    @Override
    public ClembleAspect<ManagementEvent> construct(Configuration configuration, S state) {
        switch (configuration.getPrivacyRule()) {
            case world:
                return new PublicNotificationRuleAspect(stateToKey.apply(state), PlayerAwareUtils.toPlayerList(state.getContext().getPlayerContexts()), notificationService);
            case me:
                return new PrivateNotificationRuleAspect(PlayerAwareUtils.toPlayerList(state.getContext().getPlayerContexts()), notificationService);
            default:
                throw ClembleCasinoException.withKey(ClembleCasinoError.GameSpecificationInvalid, stateToKey.apply(state));
        }
    }

    @Override
    public int getOrder(){
        return Ordered.LOWEST_PRECEDENCE - 1;
    }

}
