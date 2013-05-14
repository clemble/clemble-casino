package com.gogomaya.server.integration.game.listener;

import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameTable;

public interface GameListenerOperations<State extends GameState> {

    public GameListenerControl listen(GameTable<State> table, GameListener listener);

    public GameListenerControl listen(GameTable<State> table, GameListener listener, ListenerChannel channel);

}
