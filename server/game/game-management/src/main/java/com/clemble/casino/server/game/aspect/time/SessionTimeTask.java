package com.clemble.casino.server.game.aspect.time;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GamePlayerClock;
import com.clemble.casino.player.PlayerAware;
import org.springframework.scheduling.TriggerContext;

import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.server.game.action.GameEventTask;
import org.springframework.web.servlet.tags.ParamAware;
import sun.security.krb5.internal.PAData;

public class SessionTimeTask implements GameEventTask, GameSessionAware {

    /**
     * Generated
     */
    private static final long serialVersionUID = -7157994014672627030L;

    final private GameSessionKey session;
    final private Collection<PlayerTimeTracker> playerTimeTrackers;

    public SessionTimeTask(GameInitiation initiation, GameContext context) {
        this.session = initiation.getSession();

        final GameSpecification specification = initiation.getSpecification();

        final Collection<GamePlayerClock> clocks = context.getClock().getClocks();
        this.playerTimeTrackers = new ArrayList<PlayerTimeTracker>(clocks.size());

        for (GamePlayerClock clock: clocks) {
            playerTimeTrackers.add(new PlayerTimeTracker(clock, specification.getTotalTimeRule()));
            playerTimeTrackers.add(new PlayerTimeTracker(clock, specification.getMoveTimeRule()));
        }
    }

    @Override
    public GameSessionKey getSession() {
        return session;
    }

    public void markMoved(PlayerAware move) {
        for (PlayerTimeTracker playerTimeTracker : playerTimeTrackers) {
            if (playerTimeTracker.getPlayer().equals(move.getPlayer())) {
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
