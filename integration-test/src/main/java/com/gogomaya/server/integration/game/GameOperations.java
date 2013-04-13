package com.gogomaya.server.integration.game;

import com.gogomaya.server.game.GameSpecification;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.configuration.SelectSpecificationOptions;
import com.gogomaya.server.game.tictactoe.action.TicTacToeTable;
import com.gogomaya.server.integration.player.Player;

public interface GameOperations {

    // TODO make a more generic
    public SelectSpecificationOptions getOptions(Player player);

    public TicTacToeTable start(Player player, GameSpecification gameSpecification);

    public void listen(GameTable<?> gameTable, GameListener gameListener);

    public void listen(GameTable<?> gameTable, GameListener gameListener, ListenerChannel listenerChannel);

}
