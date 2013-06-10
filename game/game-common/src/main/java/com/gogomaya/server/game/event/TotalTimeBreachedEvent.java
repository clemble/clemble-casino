package com.gogomaya.server.game.event;

import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.rule.time.TotalTimeRule;

public class TotalTimeBreachedEvent<State extends GameState> extends TimeBreachEvent<State> {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = 6283444607013755399L;

    private long playerId;

    private TotalTimeRule totalTimeRule;

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public TotalTimeRule getTotalTimeRule() {
        return totalTimeRule;
    }

    public void setTotalTimeRule(TotalTimeRule totalTimeRule) {
        this.totalTimeRule = totalTimeRule;
    }

}
