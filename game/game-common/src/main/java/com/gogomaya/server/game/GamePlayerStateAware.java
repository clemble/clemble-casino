package com.gogomaya.server.game;

import java.util.Collection;

public interface GamePlayerStateAware {

    public Collection<GamePlayerState> getPlayerStates();

    public GamePlayerState getPlayerState(long playerId);

    public GameState setPlayerState(GamePlayerState player);

    public GameState setPlayerStates(Collection<GamePlayerState> playersStates);

}
