package com.clemble.casino.server.game.aspect.notification;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Collection;

import com.clemble.casino.game.GameWebMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.server.game.aspect.BasicGameAspect;
import com.clemble.casino.server.player.notification.PlayerNotificationService;

public class PublicNotificationRuleAspect extends BasicGameAspect<GameManagementEvent> {

    final private Logger LOG = LoggerFactory.getLogger(PublicNotificationRuleAspect.class);

    final private String tableChannel;
    final private Collection<String> participants;
    final private PlayerNotificationService notificationService;

    public PublicNotificationRuleAspect(String sessionKey, Collection<String> participants, PlayerNotificationService notificationService) {
        super(new EventTypeSelector(GameManagementEvent.class));
        this.tableChannel = GameWebMapping.toTable(sessionKey);
        this.participants = checkNotNull(participants);
        this.notificationService = checkNotNull(notificationService);
    }

    @Override
    public void doEvent(GameManagementEvent event) {
        // Step 1. Making public notification
        boolean tableNotified = notificationService.notify(tableChannel, event);
        // Step 2. Sending to exact participants
        boolean playersNotified = notificationService.notify(participants, event);
        // Step 3. Logging results
        LOG.debug("Published {} table({}) & players ({}) ", event, tableNotified, playersNotified);
    }

}
