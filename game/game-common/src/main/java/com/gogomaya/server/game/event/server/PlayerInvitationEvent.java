package com.gogomaya.server.game.event.server;

import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.specification.GameSpecificationAware;
import com.gogomaya.server.player.PlayerAware;

public class PlayerInvitationEvent implements ServerConstructionEvent, PlayerAware, GameSpecificationAware {

    /**
     * Generated 02/06/2013
     */
    private static final long serialVersionUID = 1753173974867187325L;

    private long session;

    private long playerId;

    private GameSpecification specification;

    @Override
    public long getPlayerId() {
        return playerId;
    }

    public PlayerInvitationEvent setPlayerId(Long playerId) {
        this.playerId = playerId;
        return this;
    }

    @Override
    public GameSpecification getSpecification() {
        return specification;
    }

    public PlayerInvitationEvent setSpecification(GameSpecification specification) {
        this.specification = specification;
        return this;
    }

    @Override
    public long getSession() {
        return session;
    }

    public PlayerInvitationEvent setSession(long session) {
        this.session = session;
        return this;
    }

}
