package com.clemble.casino.server.game.aspect.time;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.scheduling.TriggerContext;

import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.client.GameAction;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.server.game.action.GameEventTask;

public class SessionTimeTask implements GameEventTask, GameSessionAware {

    /**
     * Generated
     */
    private static final long serialVersionUID = -7157994014672627030L;

    final private GameSessionKey session;
    final private PlayerTimeTracker[] playerTimeTrackers;

    public SessionTimeTask(GameInitiation initiation) {
        this.session = initiation.getSession();

        final GameSpecification specification = initiation.getSpecification();

        final Collection<String> participants = initiation.getParticipants();
        this.playerTimeTrackers = new PlayerTimeTracker[participants.size() * 2];

        int pointer = 0;
        for (String participant : participants) {
            playerTimeTrackers[pointer++] = new PlayerTimeTracker(participant, specification.getTotalTimeRule());
            playerTimeTrackers[pointer++] = new PlayerTimeTracker(participant, specification.getMoveTimeRule());
        }
    }

    @Override
    public GameSessionKey getSession() {
        return session;
    }

    public void markMoved(GameAction move) {
        for (PlayerTimeTracker playerTimeTracker : playerTimeTrackers) {
            if (playerTimeTracker.getPlayer().equals(move.getPlayer())) {
                playerTimeTracker.markMoved();
            }
        }
    }

    public void markToMove(GameAction nextMove) {
        for (PlayerTimeTracker playerTimeTracker : playerTimeTrackers) {
            if (playerTimeTracker.getPlayer().equals(nextMove.getPlayer())) {
                playerTimeTracker.markToMove();
            }
        }
    }

    public long getBreachTime() {
        long breachTime = PlayerTimeTracker.DEFAULT_BREACH_TIME;
        for (PlayerTimeTracker playerTimeTracker : playerTimeTrackers) {
            if (playerTimeTracker.getBreachTime() < breachTime) {
                breachTime = playerTimeTracker.getBreachTime();
            }
        }
        return breachTime;
    }

    public Collection<GameAction> execute() {
        Collection<GameAction> breachEvents = new ArrayList<>();
        for (PlayerTimeTracker playerTimeTracker : playerTimeTrackers)
            playerTimeTracker.appendBreachEvent(breachEvents);
        return breachEvents;
    }

    public Date nextExecutionTime(TriggerContext triggerContext) {
        long breachTime = PlayerTimeTracker.DEFAULT_BREACH_TIME;
        for (PlayerTimeTracker playerTimeTracker : playerTimeTrackers) {
            if (playerTimeTracker.getBreachTime() < breachTime) {
                breachTime = playerTimeTracker.getBreachTime();
            }
        }
        return new Date(breachTime);
    }

}
