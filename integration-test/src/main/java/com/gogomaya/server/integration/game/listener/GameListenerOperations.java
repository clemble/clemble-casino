package com.gogomaya.server.integration.game.listener;

import com.gogomaya.server.game.action.GameTable;

public interface GameListenerOperations<T extends GameTable<?>> {

    public void listen(T gameTable, GameListener<T> gameListener);

    public void listen(T gameTable, GameListener<T> gameListener, ListenerChannel listenerChannel);

}
