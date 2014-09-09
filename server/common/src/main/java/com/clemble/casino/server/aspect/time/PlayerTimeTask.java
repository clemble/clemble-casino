package com.clemble.casino.server.aspect.time;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.clemble.casino.event.Event;
import com.clemble.casino.server.executor.EventTask;
import org.springframework.scheduling.TriggerContext;

import com.clemble.casino.player.PlayerAware;

public class PlayerTimeTask implements EventTask {

    /**
     * Generated 29/12/13
     */
    private static final long serialVersionUID = -7157994014672627030L;

    final private String key;
    final private Collection<PlayerTimeTracker> playerTimeTrackers;

    public PlayerTimeTask(String key, Collection<PlayerTimeTracker> playerTimeTrackers) {
        this.key = key;
        this.playerTimeTrackers = playerTimeTrackers;
    }

    public String getKey() {
        return key;
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
