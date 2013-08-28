package com.gogomaya.server.player.state;

import com.gogomaya.server.player.PlayerState;

public interface PlayerStateListener {

    public void onUpdate(long playerId, PlayerState state);

}
