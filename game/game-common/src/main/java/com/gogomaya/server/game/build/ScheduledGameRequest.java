package com.gogomaya.server.game.build;

import java.util.Collection;

import com.gogomaya.server.game.specification.GameSpecification;

public class ScheduledGameRequest implements GameRequest {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = -5026198091605671710L;

    private long scheduledGameId;

    private long playerId;

    private Collection<Long> playerIds;

    private Collection<Long> acceptedPlayersIds;

    private Collection<Long> declinedPlayersIds;

    private GameSpecification specification;

    private GameTrigger trigger;

    private GameDeclineBehavior declineBehavior;

    public long getScheduledGameId() {
        return scheduledGameId;
    }

    public ScheduledGameRequest setScheduledGameId(long scheduledGameId) {
        this.scheduledGameId = scheduledGameId;
        return this;
    }

    @Override
    public GameSpecification getSpecification() {
        return specification;
    }

    public ScheduledGameRequest setSpecification(GameSpecification specification) {
        this.specification = specification;
        return this;
    }

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

    public Collection<Long> getDeclinedPlayersIds() {
        return declinedPlayersIds;
    }

    public void setDeclinedPlayersIds(Collection<Long> declinedPlayersIds) {
        this.declinedPlayersIds = declinedPlayersIds;
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    public GameTrigger getTrigger() {
        return trigger;
    }

    public void setTrigger(GameTrigger trigger) {
        this.trigger = trigger;
    }

    public GameDeclineBehavior getDeclineBehavior() {
        return declineBehavior;
    }

    public void setDeclineBehavior(GameDeclineBehavior declineBehavior) {
        this.declineBehavior = declineBehavior;
    }

}
