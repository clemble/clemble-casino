package com.gogomaya.server.integration.game.listener;

import com.gogomaya.server.game.action.GameTable;

public interface GameListenerOperations<T extends GameTable<?>> {

    public GameListenerControl listen(T gameTable, GameListener<T> gameListener);

    public GameListenerControl listen(T gameTable, GameListener<T> gameListener, ListenerChannel listenerChannel);

}
