package com.clemble.casino.server.game.aspect.time;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Date;

import com.clemble.casino.base.ActionLatch;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.game.event.server.GameStateChangedEvent;
import com.clemble.casino.game.event.server.PlayerMovedEvent;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.game.action.GameEventTaskExecutor;
import com.clemble.casino.server.game.aspect.BasicGameAspect;

public class GameTimeAspect extends BasicGameAspect {

    final private GameContext context;
    final private Collection<String> participants;
    final private SessionTimeTask sessionTimeTracker;
    final private GameEventTaskExecutor gameEventTaskExecutor;

    public GameTimeAspect(GameInitiation initiation, GameContext context, GameEventTaskExecutor gameEventTaskExecutor) {
        super(new EventTypeSelector(GameManagementEvent.class));
        this.context  = context;
        this.participants = initiation.getParticipants();
        this.sessionTimeTracker = new SessionTimeTask(initiation, context);
        this.gameEventTaskExecutor = checkNotNull(gameEventTaskExecutor);

    }

    @Override
    public void doEvent(Event move) {
        // Step 1. To check if we need rescheduling, first calculate time before
        Date breachTimeBeforeMove = sessionTimeTracker.nextExecutionTime(null);
        if(move instanceof GameEndedEvent) {
            gameEventTaskExecutor.cancel(sessionTimeTracker);
        } else {
            if(move instanceof GameStateChangedEvent){
                for(String player: participants)
                    sessionTimeTracker.markMoved(player);
            }else if(move instanceof PlayerMovedEvent) {
                sessionTimeTracker.markMoved((PlayerAware) move);
            }
            ActionLatch latch = context.getActionLatch();
            for (String player: participants) {
                if (latch.acted(player)) {
                    sessionTimeTracker.markMoved(latch.fetchAction(player));
                } else {
                    sessionTimeTracker.markToMove(latch.fetchAction(player));
                }
            }
        }
            // Step 3. Re scheduling if needed
        if (!sessionTimeTracker.nextExecutionTime(null).equals(breachTimeBeforeMove)) {
            gameEventTaskExecutor.reschedule(sessionTimeTracker);
        }
    }

}
