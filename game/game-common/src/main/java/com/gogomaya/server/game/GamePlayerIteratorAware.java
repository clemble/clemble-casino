package com.gogomaya.server.game;

public interface GamePlayerIteratorAware {

    public GamePlayerIterator getPlayerIterator();

    public GameState setPlayerIterator(GamePlayerIterator playerIterator);

}
