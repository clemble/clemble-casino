package com.gogomaya.server.game.construct;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("availability")
public class AvailabilityGameRequest extends GameRequest {

    /**
     * Generated 12/06/13
     */
    private static final long serialVersionUID = -3051736949418145655L;

    private GameDeclineBehavior declineBehavior;

    public GameDeclineBehavior getDeclineBehavior() {
        return declineBehavior;
    }

    public void setDeclineBehavior(GameDeclineBehavior declineBehavior) {
        this.declineBehavior = declineBehavior;
    }

}
