package com.gogomaya.server.integration.game.listener;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.player.security.PlayerSession;

public interface GameListenerOperations<State extends GameState> {

    public GameListenerControl listen(PlayerSession playerSession, GameListener listener);

    public GameListenerControl listen(PlayerSession playerSession, GameListener listener, ListenerChannel channel);

}
