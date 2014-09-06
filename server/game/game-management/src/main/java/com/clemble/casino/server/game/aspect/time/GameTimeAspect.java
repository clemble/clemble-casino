package com.clemble.casino.server.game.aspect.time;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Date;

import com.clemble.casino.base.ActionLatch;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.RoundGameContext;
import com.clemble.casino.game.event.server.RoundEndedEvent;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.game.event.server.RoundStateChangedEvent;
import com.clemble.casino.game.event.server.PlayerMovedEvent;
import com.clemble.casino.game.configuration.RoundGameConfiguration;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.player.PlayerAwareUtils;
import com.clemble.casino.server.game.action.GameEventTaskExecutor;
import com.clemble.casino.server.game.aspect.GameAspect;

public class GameTimeAspect extends GameAspect<GameManagementEvent> {

    final private RoundGameContext context;
    final private Collection<String> participants;
    final private SessionTimeTask sessionTimeTracker;
    final private GameEventTaskExecutor gameEventTaskExecutor;

    public GameTimeAspect(
            String sessionKey,
            RoundGameConfiguration specification,
            RoundGameContext context,
            GameEventTaskExecutor gameEventTaskExecutor) {
        super(new EventTypeSelector(GameManagementEvent.class));
        this.context  = context;
        this.participants = PlayerAwareUtils.toPlayerList(context.getPlayerContexts());
        this.sessionTimeTracker = new SessionTimeTask(sessionKey, specification, context);
        this.gameEventTaskExecutor = checkNotNull(gameEventTaskExecutor);

    }

    @Override
    public void doEvent(GameManagementEvent move) {
        // Step 1. To check if we need rescheduling, first calculate time before
        Date breachTimeBeforeMove = sessionTimeTracker.nextExecutionTime(null);
        if(move instanceof RoundEndedEvent) {
            gameEventTaskExecutor.cancel(sessionTimeTracker);
        } else {
            ActionLatch latch = context.getActionLatch();
            if(move instanceof RoundStateChangedEvent){
                for(String player: participants)
                    sessionTimeTracker.markMoved(player);
                for (String player: latch.fetchParticipants()) {
                    sessionTimeTracker.markToMove(player);
                }
            } else if(move instanceof PlayerMovedEvent) {
                sessionTimeTracker.markMoved((PlayerAware) move);
            }
            // Step 3. Re scheduling if needed
            if (!sessionTimeTracker.nextExecutionTime(null).equals(breachTimeBeforeMove)) {
                gameEventTaskExecutor.reschedule(sessionTimeTracker);
            }
        }
    }

}
