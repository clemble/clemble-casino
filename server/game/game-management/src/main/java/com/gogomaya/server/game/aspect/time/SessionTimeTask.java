package com.gogomaya.server.game.aspect.time;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.scheduling.TriggerContext;

import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.Game;
import com.gogomaya.game.SessionAware;
import com.gogomaya.game.construct.GameInitiation;
import com.gogomaya.game.specification.GameSpecification;
import com.gogomaya.server.game.action.GameEventTask;

public class SessionTimeTask implements GameEventTask, SessionAware {

    /**
     * Generated
     */
    private static final long serialVersionUID = -7157994014672627030L;

    final private Game game;
    final private long session;
    final private PlayerTimeTracker[] playerTimeTrackers;

    public SessionTimeTask(GameInitiation initiation) {
        this.game = initiation.getGame();
        this.session = initiation.getSession();

        final GameSpecification specification = initiation.getSpecification();

        final Collection<Long> participants = initiation.getParticipants();
        this.playerTimeTrackers = new PlayerTimeTracker[participants.size() * 2];

        int pointer = 0;
        for (Long participant : participants) {
            playerTimeTrackers[pointer++] = new PlayerTimeTracker(participant, specification.getTotalTimeRule());
            playerTimeTrackers[pointer++] = new PlayerTimeTracker(participant, specification.getMoveTimeRule());
        }
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public long getSession() {
        return session;
    }

    public void markMoved(ClientEvent move) {
        for (PlayerTimeTracker playerTimeTracker : playerTimeTrackers) {
            if (playerTimeTracker.getPlayerId() == move.getPlayerId()) {
                playerTimeTracker.markMoved();
            }
        }
    }

    public void markToMove(ClientEvent nextMove) {
        for (PlayerTimeTracker playerTimeTracker : playerTimeTrackers) {
            if (playerTimeTracker.getPlayerId() == nextMove.getPlayerId()) {
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

    public Collection<ClientEvent> execute() {
        Collection<ClientEvent> breachEvents = new ArrayList<>();
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
