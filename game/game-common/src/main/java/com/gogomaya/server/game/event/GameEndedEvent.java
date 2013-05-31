package com.gogomaya.server.game.event;

import com.gogomaya.server.game.action.GameOutcome;
import com.gogomaya.server.game.action.GameState;

public class GameEndedEvent<State extends GameState> extends GameEvent<State> {

    /**
     * Generated 07/05/13
     */
    private static final long serialVersionUID = 820200145932972096L;

    private GameOutcome outcome;

    public GameEndedEvent() {
    }

    public GameEndedEvent(State state) {
        this.setState(state);
        this.outcome = state.getOutcome();
    }

    public GameOutcome getOutcome() {
        return outcome;
    }

    public GameEndedEvent<State> setOutcome(GameOutcome outcome) {
        this.outcome = outcome;
        return this;
    }
}
