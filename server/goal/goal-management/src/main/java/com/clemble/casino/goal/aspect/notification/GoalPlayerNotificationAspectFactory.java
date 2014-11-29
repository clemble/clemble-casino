package com.clemble.casino.goal.aspect.notification;

import com.clemble.casino.goal.aspect.GoalAspectFactory;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalManagementEvent;
import com.clemble.casino.player.service.PlayerConnectionService;
import com.clemble.casino.server.aspect.ClembleAspect;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import org.springframework.core.Ordered;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by mavarazy on 11/16/14.
 */
public class GoalPlayerNotificationAspectFactory implements GoalAspectFactory<GoalManagementEvent> {

    final private PlayerConnectionService connectionService;
    final private ServerNotificationService notificationService;

    public GoalPlayerNotificationAspectFactory(PlayerConnectionService connectionService, ServerNotificationService notificationService) {
        this.connectionService = connectionService;
        this.notificationService = notificationService;
    }

    @Override
    public ClembleAspect<GoalManagementEvent> construct(GoalConfiguration configuration, GoalState state) {
        // Step 1. Generating list of participants
        Collection<String> participants = new ArrayList<>();
        participants.add(state.getPlayer());
        // Step 2. Appending additional notification configurations if needed
        switch (configuration.getPrivacyRule()) {
            // TODO extend support in future
            case friends:
            case world:
                participants.addAll(connectionService.getConnections(state.getPlayer()));
                break;
            case me:
            default:
                break;
        }
        // Step 3. Returning related notification aspect
        return new GoalPlayerNotificationAspect(participants, notificationService);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
