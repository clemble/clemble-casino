package com.clemble.casino.server.game.action;

import java.util.Date;

import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.game.action.GameAction;

public class ScheduledGameAction implements GameSessionAware, Comparable<ScheduledGameAction> {

    /**
     * Generated 10/01/14
     */
    private static final long serialVersionUID = 4925081731664108806L;

    final private String sessionKey;
    final private GameAction action;
    private Date scheduled;

    public ScheduledGameAction(String sessionKey, GameAction action, Date scheduled) {
        this.sessionKey = sessionKey;
        this.scheduled = scheduled;
        this.action = action;
    }

    @Override
    public String getSessionKey() {
        return sessionKey;
    }

    public GameAction getAction() {
        return action;
    }

    public Date getScheduled() {
        return scheduled;
    }

    public void setScheduled(Date newScheduledDate) {
        this.scheduled = newScheduledDate;
    }

    @Override
    public int compareTo(ScheduledGameAction o) {
        return o.scheduled.compareTo(scheduled);
    }

}
