package com.gogomaya.server.game.build;

import java.util.Collection;

public class ScheduledGameRequest extends GameRequest {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = -5026198091605671710L;

    private Collection<Long> playerIds;
    private Collection<Long> acceptedPlayersIds;

    private GameDeclineBehavior declineBehavior;

    public Collection<Long> getPlayerIds() {
        return playerIds;
    }

    public ScheduledGameRequest setPlayerIds(Collection<Long> playerIds) {
        this.playerIds = playerIds;
        return this;
    }

    public Collection<Long> getAcceptedPlayersIds() {
        return acceptedPlayersIds;
    }

    public void setAcceptedPlayersIds(Collection<Long> acceptedPlayersIds) {
        this.acceptedPlayersIds = acceptedPlayersIds;
    }

    public GameDeclineBehavior getDeclineBehavior() {
        return declineBehavior;
    }

    public void setDeclineBehavior(GameDeclineBehavior declineBehavior) {
        this.declineBehavior = declineBehavior;
    }

}
