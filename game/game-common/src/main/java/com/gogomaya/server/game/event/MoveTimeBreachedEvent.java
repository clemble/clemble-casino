package com.gogomaya.server.game.event;

import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.rule.time.MoveTimeRule;

public class MoveTimeBreachedEvent<State extends GameState> extends TimeBreachEvent<State> {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = -373442967753806632L;

    private long playerId;

    private MoveTimeRule moveTimeRule;

    public MoveTimeBreachedEvent() {
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public MoveTimeRule getMoveTimeRule() {
        return moveTimeRule;
    }

    public void setMoveTimeRule(MoveTimeRule moveTimeRule) {
        this.moveTimeRule = moveTimeRule;
    }
}
