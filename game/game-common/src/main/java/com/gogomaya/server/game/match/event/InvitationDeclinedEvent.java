package com.gogomaya.server.game.match.event;

import com.gogomaya.server.player.PlayerAware;


public class InvitationDeclinedEvent extends ScheduledGameEvent implements PlayerAware {

    /**
     * Generated 02/06/13
     */
    private static final long serialVersionUID = 655381424177654890L;

    private long scheduledGameId;

    private long playerId;

    @Override
    public long getScheduledGameId() {
        return scheduledGameId;
    }

    public void setScheduledGameId(long scheduledGameId) {
        this.scheduledGameId = scheduledGameId;
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

}
