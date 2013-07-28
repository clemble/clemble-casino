package com.gogomaya.server.game.aspect.time;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.scheduling.TriggerContext;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.Game;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.SessionAware;
import com.gogomaya.server.game.action.GameEventTask;
import com.gogomaya.server.game.rule.time.TimeRule;

public class SessionTimerTask implements GameEventTask, SessionAware {

    /**
     * Generated
     */
    private static final long serialVersionUID = -7157994014672627030L;

    final private Game game;
    final private long session;
    final private PlayerTimeTracker[] playerTimeTrackers;

    public SessionTimerTask(GameSession<?> session, TimeRule timeRule) {
        this.game = session.getSpecification().getName().getGame();
        this.session = session.getSession();
        this.playerTimeTrackers = new PlayerTimeTracker[session.getPlayers().size()];
        for (int i = 0; i < session.getPlayers().size(); i++) {
            playerTimeTrackers[i] = new PlayerTimeTracker(session.getPlayers().get(i), timeRule);
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

    public boolean markMoved(ClientEvent move) {
        long beforeMove = getBreachTime();

        for (PlayerTimeTracker playerTimeTracker : playerTimeTrackers) {
            if (playerTimeTracker.getPlayerId() == move.getPlayerId()) {
                playerTimeTracker.markMoved();
                break;
            }
        }

        return getBreachTime() == beforeMove;
    }

    public boolean markToMove(ClientEvent nextMove) {
        long beforeMove = getBreachTime();

        for (PlayerTimeTracker playerTimeTracker : playerTimeTrackers) {
            if (playerTimeTracker.getPlayerId() == nextMove.getPlayerId()) {
                playerTimeTracker.markToMove();
                break;
            }
        }

        return getBreachTime() == beforeMove;
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
