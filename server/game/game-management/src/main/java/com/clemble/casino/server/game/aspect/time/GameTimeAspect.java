package com.clemble.casino.server.game.aspect.time;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.clemble.casino.ActionLatch;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.RoundGameContext;
import com.clemble.casino.game.RoundGamePlayerContext;
import com.clemble.casino.game.event.RoundEndedEvent;
import com.clemble.casino.game.event.GameManagementEvent;
import com.clemble.casino.game.event.RoundStateChangedEvent;
import com.clemble.casino.game.event.PlayerMovedEvent;
import com.clemble.casino.game.configuration.RoundGameConfiguration;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.player.PlayerAwareUtils;
import com.clemble.casino.server.aspect.time.PlayerTimeTask;
import com.clemble.casino.server.aspect.time.PlayerTimeTracker;
import com.clemble.casino.server.executor.EventTaskExecutor;
import com.clemble.casino.server.game.aspect.GameAspect;

public class GameTimeAspect extends GameAspect<GameManagementEvent> {

    final private RoundGameContext context;
    final private Collection<String> participants;
    final private PlayerTimeTask sessionTimeTracker;
    final private EventTaskExecutor eventTaskExecutor;

    public GameTimeAspect(
            String sessionKey,
            RoundGameConfiguration specification,
            RoundGameContext context,
            EventTaskExecutor eventTaskExecutor) {
        super(new EventTypeSelector(GameManagementEvent.class));
        this.context  = context;
        this.participants = PlayerAwareUtils.toPlayerList(context.getPlayerContexts());
        this.eventTaskExecutor = checkNotNull(eventTaskExecutor);

        List<PlayerTimeTracker> playerTimeTrackers = new ArrayList<PlayerTimeTracker>();
        for (RoundGamePlayerContext playerContext : context.getPlayerContexts()) {
            playerTimeTrackers.add(new PlayerTimeTracker(playerContext.getPlayer(), playerContext.getClock(), specification.getTotalTimeRule()));
            playerTimeTrackers.add(new PlayerTimeTracker(playerContext.getPlayer(), playerContext.getClock(), specification.getMoveTimeRule()));
        }
        this.sessionTimeTracker = new PlayerTimeTask(sessionKey, playerTimeTrackers);
    }

    @Override
    public void doEvent(GameManagementEvent move) {
        // Step 1. To check if we need rescheduling, first calculate time before
        Date breachTimeBeforeMove = sessionTimeTracker.nextExecutionTime(null);
        if(move instanceof RoundEndedEvent) {
            eventTaskExecutor.cancel(sessionTimeTracker);
        } else {
            ActionLatch latch = context.getActionLatch();
            if(move instanceof RoundStateChangedEvent){
                // Step 2.1. Mark all participants as moved
                participants.forEach(sessionTimeTracker::markMoved);
                // Step 2.2. Mark all pending participants as to move
                latch.fetchParticipants().forEach(sessionTimeTracker::markToMove);
            } else if(move instanceof PlayerMovedEvent) {
                sessionTimeTracker.markMoved((PlayerAware) move);
            }
            // Step 3. Re scheduling if needed
            if (!sessionTimeTracker.nextExecutionTime(null).equals(breachTimeBeforeMove)) {
                eventTaskExecutor.reschedule(sessionTimeTracker);
            }
        }
    }

}
