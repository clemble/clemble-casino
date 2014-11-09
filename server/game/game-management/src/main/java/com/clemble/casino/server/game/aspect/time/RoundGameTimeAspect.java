package com.clemble.casino.server.game.aspect.time;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.*;

import com.clemble.casino.ActionLatch;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.lifecycle.management.RoundGameContext;
import com.clemble.casino.game.lifecycle.management.RoundGamePlayerContext;
import com.clemble.casino.game.lifecycle.management.event.GamePlayerMovedEvent;
import com.clemble.casino.game.lifecycle.management.event.RoundChangedEvent;
import com.clemble.casino.game.lifecycle.management.event.RoundEndedEvent;
import com.clemble.casino.game.lifecycle.management.event.GameManagementEvent;
import com.clemble.casino.game.lifecycle.configuration.RoundGameConfiguration;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.aspect.time.PlayerClockTimeoutEventTask;
import com.clemble.casino.server.event.game.SystemGameTimeoutEvent;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.player.notification.SystemNotificationService;

public class RoundGameTimeAspect extends GameAspect<GameManagementEvent> {

    final private Map<String, PlayerClockTimeoutEventTask> playerToTask = new HashMap<>();

    public RoundGameTimeAspect(
            RoundGameConfiguration configuration,
            RoundGameContext context,
            SystemNotificationService systemNotificationService) {
        super(new EventTypeSelector(GameManagementEvent.class));

        context.getPlayerContexts().forEach(playerContext -> {
            playerToTask.put(playerContext.getPlayer(),
                new PlayerClockTimeoutEventTask(
                    context.getSessionKey(),
                    playerContext.getPlayer(),
                    playerContext.getClock(),
                    configuration.getMoveTimeRule(),
                    configuration.getTotalTimeRule(),
                    systemNotificationService,
                    (sessionKey) -> new SystemGameTimeoutEvent(sessionKey)));
        });
    }

    @Override
    protected void doEvent(GameManagementEvent move) {
        // Step 1. Stopping all events
        if(move instanceof RoundEndedEvent || move instanceof RoundChangedEvent) {
            playerToTask.values().forEach(task -> {
                task.stop();
            });
        }
        // Step 2. Starting if needed new track sys
        if(move instanceof RoundChangedEvent) {
            ActionLatch latch = ((RoundChangedEvent) move).getState().getContext().getActionLatch();
            // Step 2.1. Mark all participants as moved
            latch.fetchParticipants().forEach(participant -> {
                playerToTask.get(participant).start();
            });
        } else if(move instanceof GamePlayerMovedEvent) {
            playerToTask.get(((PlayerAware) move).getPlayer()).stop();
        }
    }

}
