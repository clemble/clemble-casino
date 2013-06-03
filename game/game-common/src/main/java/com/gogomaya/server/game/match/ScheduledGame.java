package com.gogomaya.server.game.match;

import java.util.Collection;
import java.util.Date;

import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.specification.GameSpecificationAware;

public class ScheduledGame implements GameSpecificationAware {

    private long scheduledGameId;

    private Collection<Long> playerIds;

    private Collection<Long> acceptedPlayersIds;

    private Collection<Long> declinedPlayersIds;

    private GameSpecification specification;

    private Date scheduledTime;

    public long getScheduledGameId() {
        return scheduledGameId;
    }

    public ScheduledGame setScheduledGameId(long scheduledGameId) {
        this.scheduledGameId = scheduledGameId;
        return this;
    }

    @Override
    public GameSpecification getSpecification() {
        return specification;
    }

    public ScheduledGame setSpecification(GameSpecification specification) {
        this.specification = specification;
        return this;
    }

    public Collection<Long> getPlayerIds() {
        return playerIds;
    }

    public ScheduledGame setPlayerIds(Collection<Long> playerIds) {
        this.playerIds = playerIds;
        return this;
    }

    public Date getScheduledTime() {
        return scheduledTime;
    }

    public ScheduledGame setScheduledTime(Date scheduledTime) {
        this.scheduledTime = scheduledTime;
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

}
