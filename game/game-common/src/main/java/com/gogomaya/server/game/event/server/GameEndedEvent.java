package com.gogomaya.server.game.event.server;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.server.game.GameOutcome;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.SessionAware;

@JsonTypeName("ended")
public class GameEndedEvent<State extends GameState> extends GameServerEvent<State> {

    /**
     * Generated 07/05/13
     */
    private static final long serialVersionUID = 820200145932972096L;

    private GameOutcome outcome;

    public GameEndedEvent(){
    }

    public GameEndedEvent(SessionAware sessionAware) {
        super(sessionAware);
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((outcome == null) ? 0 : outcome.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GameEndedEvent other = (GameEndedEvent) obj;
        if (outcome == null) {
            if (other.outcome != null)
                return false;
        } else if (!outcome.equals(other.outcome))
            return false;
        return true;
    }
}
