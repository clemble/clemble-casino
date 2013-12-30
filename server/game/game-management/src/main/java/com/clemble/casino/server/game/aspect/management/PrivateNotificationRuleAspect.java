package com.clemble.casino.server.game.aspect.management;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Collection;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.game.aspect.BasicGameAspect;
import com.clemble.casino.server.player.notification.PlayerNotificationService;

public class PrivateNotificationRuleAspect extends BasicGameAspect<GameManagementEvent>{

    final private Collection<? extends PlayerAware> participants;
    final private PlayerNotificationService notificationService;

    public PrivateNotificationRuleAspect(GameInitiation initiation, PlayerNotificationService notificationService) {
        super(new EventTypeSelector(GameManagementEvent.class));
        this.participants = checkNotNull(initiation).getParticipants();
        this.notificationService = checkNotNull(notificationService);
    }


    @Override
    public void doEvent(GameManagementEvent event) {
        // Step 1. Sending only to participants
        notificationService.notifyAll(participants, event);
    }

}
