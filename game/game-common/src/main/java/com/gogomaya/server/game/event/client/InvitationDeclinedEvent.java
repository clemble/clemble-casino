package com.gogomaya.server.game.event.client;

public class InvitationDeclinedEvent implements ClientConstructionEvent {

    /**
     * Generated 02/06/13
     */
    private static final long serialVersionUID = 655381424177654890L;

    private long playerId;

    private long session;

    @Override
    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    @Override
    public long getSession() {
        return session;
    }

}
