package com.clemble.casino.server.game.aspect.time;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.scheduling.TriggerContext;

import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GamePlayerContext;
import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.game.action.GameEventTask;

public class SessionTimeTask implements GameEventTask, GameSessionAware {

    /**
     * Generated 29/12/13
     */
    private static final long serialVersionUID = -7157994014672627030L;

    final private GameSessionKey session;
    final private Collection<PlayerTimeTracker> playerTimeTrackers;

    public SessionTimeTask(GameInitiation initiation, GameContext context) {
        this.session = initiation.getSession();

        final GameSpecification specification = initiation.getSpecification();

        this.playerTimeTrackers = new ArrayList<PlayerTimeTracker>();
        for (GamePlayerContext playerContext : context.getPlayerContexts()) {
            playerTimeTrackers.add(new PlayerTimeTracker(playerContext.getClock(), specification.getTotalTimeRule()));
            playerTimeTrackers.add(new PlayerTimeTracker(playerContext.getClock(), specification.getMoveTimeRule()));
        }
    }

    @Override
    public GameSessionKey getSession() {
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
        for (PlayerTimeTracker playerTimeTracker : playerTimeTrackers) {
            if (playerTimeTracker.getPlayer().equals(nextMove.getPlayer())) {
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
