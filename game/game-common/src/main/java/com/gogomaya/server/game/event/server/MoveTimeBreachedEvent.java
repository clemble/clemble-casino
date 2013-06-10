package com.gogomaya.server.game.event.server;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.rule.time.MoveTimeRule;

public class MoveTimeBreachedEvent<State extends GameState> extends AbstractTimeBreachEvent<State> {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = -373442967753806632L;

    private MoveTimeRule moveTimeRule;

    public MoveTimeBreachedEvent() {
    }

    public MoveTimeRule getMoveTimeRule() {
        return moveTimeRule;
    }

    public void setMoveTimeRule(MoveTimeRule moveTimeRule) {
        this.moveTimeRule = moveTimeRule;
    }
}
