package com.gogomaya.server.game.active.time;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.event.client.MoveTimeoutSurrenderEvent;
import com.gogomaya.server.game.event.client.TotalTimeoutSurrenderEvent;
import com.gogomaya.server.game.rule.time.MoveTimeRule;
import com.gogomaya.server.game.rule.time.TimeRule;
import com.gogomaya.server.player.PlayerAware;

public class GameTimeBreach implements Comparable<GameTimeBreach>, PlayerAware {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = -8537249165242972837L;

    final private long playerId;
    final private long breachTime;
    final private TimeRule timeRule;

    public GameTimeBreach(long playerId, long breachTime, TimeRule timeRule) {
        this.playerId = playerId;
        this.breachTime = breachTime;
        this.timeRule = timeRule;
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    public TimeRule getTimeRule() {
        return timeRule;
    }

    public long getBreachTime() {
        return breachTime;
    }

    public boolean breached() {
        return System.currentTimeMillis() >= breachTime;
    }

    public ClientEvent toClientEvent() {
        if (breached()) {
            switch (timeRule.getPunishment()) {
            default:
                break;
            case loose:
                if (timeRule instanceof MoveTimeRule) {
                    return new MoveTimeoutSurrenderEvent(playerId);
                } else {
                    return new TotalTimeoutSurrenderEvent(playerId);
                }
            }
        }
        return null;
    }

    @Override
    public int compareTo(GameTimeBreach o) {
        return (int) (getBreachTime() - o.getBreachTime());
    }

}