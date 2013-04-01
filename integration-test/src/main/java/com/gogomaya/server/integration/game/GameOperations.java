package com.gogomaya.server.integration.game;

import com.gogomaya.server.game.GameSpecification;
import com.gogomaya.server.game.table.GameTable;
import com.gogomaya.server.integration.player.Player;

public interface GameOperations {

    public GameTable create(Player player);

    public GameTable create(Player player, GameSpecification gameSpecification);

    public void addListener(GameTable gameTable, GameListener gameListener);

    public void addListener(GameTable gameTable, GameListener gameListener, ListenerChannel listenerChannel);

}
