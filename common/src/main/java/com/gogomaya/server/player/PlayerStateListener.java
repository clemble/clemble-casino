package com.gogomaya.server.player;

public interface PlayerStateListener {

    public void onUpdate(long playerId, PlayerState state);

}
