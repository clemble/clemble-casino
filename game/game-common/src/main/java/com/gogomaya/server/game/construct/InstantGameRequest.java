package com.gogomaya.server.game.construct;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("instant")
public class InstantGameRequest extends GameRequest {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = 4949060894194971610L;

    private GameDeclineBehavior declineBehavior;

    public GameDeclineBehavior getDeclineBehavior() {
        return declineBehavior;
    }

    public void setDeclineBehavior(GameDeclineBehavior declineBehavior) {
        this.declineBehavior = declineBehavior;
    }

}