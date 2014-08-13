package com.clemble.casino.server.game.aspect.time;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.clemble.casino.game.RoundGameContext;
import com.clemble.casino.game.RoundGamePlayerContext;
import com.clemble.casino.game.specification.RoundGameConfiguration;
import org.springframework.scheduling.TriggerContext;

import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.game.action.GameEventTask;

public class SessionTimeTask implements GameEventTask, GameSessionAware {

    /**
     * Generated 29/12/13
     */
    private static final long serialVersionUID = -7157994014672627030L;

    final private GameSessionKey session;
    final private Collection<PlayerTimeTracker> playerTimeTrackers;

    public SessionTimeTask(GameSessionKey sessionKey, RoundGameConfiguration initiation, RoundGameContext context) {
        this.session = sessionKey;

        final RoundGameConfiguration specification = initiation;

        this.playerTimeTrackers = new ArrayList<PlayerTimeTracker>();
        for (RoundGamePlayerContext playerContext : context.getPlayerContexts()) {
            playerTimeTrackers.add(new PlayerTimeTracker(playerContext.getPlayer(), playerContext.getClock(), specification.getTotalTimeRule()));
            playerTimeTrackers.add(new PlayerTimeTracker(playerContext.getPlayer(), playerContext.getClock(), specification.getMoveTimeRule()));
        }
    }

    @Override
    public GameSessionKey getSessionKey() {
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

    public Collection<GameAction> execute() {
        Collection<GameAction> breachEvents = new ArrayList<>();
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
