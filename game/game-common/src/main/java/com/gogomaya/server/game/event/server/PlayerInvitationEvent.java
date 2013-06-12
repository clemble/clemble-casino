package com.gogomaya.server.game.event.server;

import com.gogomaya.server.game.build.InitiatorAware;
import com.gogomaya.server.game.event.ScheduledGameEvent;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.specification.GameSpecificationAware;
import com.gogomaya.server.player.PlayerAware;

public class PlayerInvitationEvent extends ScheduledGameEvent implements InitiatorAware, ServerConstructionEvent, PlayerAware, GameSpecificationAware {

    /**
     * Generated 02/06/2013
     */
    private static final long serialVersionUID = 1753173974867187325L;

    private long initiator;

    private long scheduledGameId;

    private long playerId;

    private GameSpecification specification;

    @Override
    public Long getInitiator() {
        return initiator;
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    @Override
    public GameSpecification getSpecification() {
        return specification;
    }

    public void setSpecification(GameSpecification specification) {
        this.specification = specification;
    }

    @Override
    public long getScheduledGameId() {
        return scheduledGameId;
    }

    public void setScheduledGameId(long scheduledGameId) {
        this.scheduledGameId = scheduledGameId;
    }

}
