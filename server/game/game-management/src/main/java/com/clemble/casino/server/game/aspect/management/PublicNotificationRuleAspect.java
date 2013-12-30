package com.clemble.casino.server.game.aspect.management;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Collection;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.event.NotificationMapping;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.game.aspect.BasicGameAspect;
import com.clemble.casino.server.player.notification.PlayerNotificationService;

public class PublicNotificationRuleAspect extends BasicGameAspect<GameManagementEvent> {

    final private String tableChannel;
    final private GameSessionKey sessionKey;
    final private Collection<? extends PlayerAware> participants;
    final private PlayerNotificationService notificationService;

    public PublicNotificationRuleAspect(GameInitiation initiation, PlayerNotificationService notificationService) {
        super(new EventTypeSelector(GameManagementEvent.class));
        this.sessionKey = checkNotNull(initiation.getSession());
        this.tableChannel = NotificationMapping.toTable(sessionKey);
        this.participants = initiation.getParticipants();
        this.notificationService = checkNotNull(notificationService);
    }

    @Override
    public void doEvent(GameManagementEvent event) {
        // Step 1. Making public notification
        notificationService.notify(tableChannel, event);
        // Step 2. Sending to exact participants
        notificationService.notifyAll(participants, event);
    }

}
