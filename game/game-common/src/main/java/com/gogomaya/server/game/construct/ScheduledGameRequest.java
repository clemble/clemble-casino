package com.gogomaya.server.game.construct;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("scheduled")
public class ScheduledGameRequest extends GameRequest {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = -5026198091605671710L;

    private GameDeclineBehavior declineBehavior;

    public GameDeclineBehavior getDeclineBehavior() {
        return declineBehavior;
    }

    public void setDeclineBehavior(GameDeclineBehavior declineBehavior) {
        this.declineBehavior = declineBehavior;
    }

}
