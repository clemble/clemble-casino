package com.gogomaya.server.game;

import java.io.Serializable;

public interface GameAware<T extends GameAware<T>> extends Serializable {

    public String getGameName();

    public T setGameName(String newGameName);

}
