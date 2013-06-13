package com.gogomaya.server.game.event.client;

public class InvitationAcceptedEvent implements ClientConstructionEvent {

    /**
     * Generated 02/06/2013
     */
    private static final long serialVersionUID = -4465974655141746411L;

    private long playerId;

    private long session;

    @Override
    public long getPlayerId() {
        return playerId;
    }

    @Override
    public long getSession() {
        return session;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

}
