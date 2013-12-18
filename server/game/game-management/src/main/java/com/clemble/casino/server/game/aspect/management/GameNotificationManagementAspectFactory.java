package com.clemble.casino.server.game.aspect.management;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Collection;

import com.clemble.casino.event.Event;
import com.clemble.casino.event.NotificationMapping;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.rule.construct.PrivacyRule;
import com.clemble.casino.server.game.aspect.BasicGameManagementAspect;
import com.clemble.casino.server.game.aspect.GameManagementAspect;
import com.clemble.casino.server.game.aspect.GameManagementAspecteFactory;
import com.clemble.casino.server.player.notification.PlayerNotificationService;

public class GameNotificationManagementAspectFactory implements GameManagementAspecteFactory {

    final private PlayerNotificationService notificationService;

    public GameNotificationManagementAspectFactory(PlayerNotificationService notificationService) {
        this.notificationService = checkNotNull(notificationService);
    }

    @Override
    public GameManagementAspect construct(GameInitiation initiation) {
        PrivacyRule privacyRule = initiation.getSpecification().getPrivacyRule();
        switch (privacyRule) {
        case everybody:
            return new PublicNotificationManagementAspect(initiation, notificationService);
        case players:
        default:
            return new PrivateNotificationManagementAspect(initiation, notificationService);
        }
    }

    final private static class PrivateNotificationManagementAspect extends BasicGameManagementAspect {

        final private Collection<String> participants;
        final private PlayerNotificationService notificationService;

        public PrivateNotificationManagementAspect(GameInitiation initiation, PlayerNotificationService notificationService) {
            this.participants = checkNotNull(initiation.getParticipants());
            this.notificationService = checkNotNull(notificationService);
        }

        @Override
        public <State extends GameState> void changed(State state, Collection<Event> events) {
            // Step 1. Sending only to participants
            notificationService.notify(participants, events);
        }

    }

    final private static class PublicNotificationManagementAspect extends BasicGameManagementAspect {

        final private String tableChannel;
        final private GameSessionKey sessionKey;
        final private Collection<String> participants;
        final private PlayerNotificationService notificationService;

        public PublicNotificationManagementAspect(GameInitiation initiation, PlayerNotificationService notificationService) {
            this.sessionKey = checkNotNull(initiation.getSession());
            this.tableChannel = NotificationMapping.toTable(sessionKey);
            this.participants = checkNotNull(initiation.getParticipants());
            this.notificationService = checkNotNull(notificationService);
        }

        @Override
        public <State extends GameState> void changed(State state, Collection<Event> events) {
            // Step 1. Making public notification
            notificationService.notify(tableChannel, events);
            // Step 2. Sending to exact participants
            notificationService.notify(participants, events);
        }

    }

}
