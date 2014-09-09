package com.clemble.casino.server.game.aspect.time;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.RoundGameContext;
import com.clemble.casino.game.RoundGamePlayerContext;
import com.clemble.casino.game.configuration.RoundGameConfiguration;
import com.clemble.casino.server.executor.EventTask;
import org.springframework.scheduling.TriggerContext;

import com.clemble.casino.player.PlayerAware;

public class SessionTimeTask implements EventTask {

    /**
     * Generated 29/12/13
     */
    private static final long serialVersionUID = -7157994014672627030L;

    final private String session;
    final private Collection<PlayerTimeTracker> playerTimeTrackers;

    public SessionTimeTask(String sessionKey, RoundGameConfiguration initiation, RoundGameContext context) {
        this.session = sessionKey;

        final RoundGameConfiguration specification = initiation;

        this.playerTimeTrackers = new ArrayList<PlayerTimeTracker>();
        for (RoundGamePlayerContext playerContext : context.getPlayerContexts()) {
            playerTimeTrackers.add(new PlayerTimeTracker(playerContext.getPlayer(), playerContext.getClock(), specification.getTotalTimeRule()));
            playerTimeTrackers.add(new PlayerTimeTracker(playerContext.getPlayer(), playerContext.getClock(), specification.getMoveTimeRule()));
        }
    }

    public String getKey() {
        return session;
    }

    public void markMoved(PlayerAware move) {
        markMoved(move.getPlayer());
    }

    public void markMoved(String player) {
        for (PlayerTimeTracker playerTimeTracker : playerTimeTrackers) {
            if (playerTimeTracker.getPlayer().equals(player)) {
                playerTimeTracker.markMoved();
            }
        }
    }

    public void markToMove(PlayerAware nextMove) {
        markToMove(nextMove.getPlayer());
    }

    public void markToMove(String player) {
        for (PlayerTimeTracker playerTimeTracker : playerTimeTrackers) {
            if (playerTimeTracker.getPlayer().equals(player)) {
                playerTimeTracker.markToMove();
            }
        }
    }

    public Collection<Event> execute() {
        Collection<Event> breachEvents = new ArrayList<>();
        for (PlayerTimeTracker playerTimeTracker : playerTimeTrackers)
            playerTimeTracker.appendBreachEvent(breachEvents);
        return breachEvents;
    }

    public Date nextExecutionTime(TriggerContext triggerContext) {
        long breachTime = Long.MAX_VALUE;
        for (PlayerTimeTracker playerTimeTracker : playerTimeTrackers) {
            if (playerTimeTracker.getBreachTime() < breachTime) {
                breachTime = playerTimeTracker.getBreachTime();
            }
        }
        return new Date(breachTime);
    }

}
