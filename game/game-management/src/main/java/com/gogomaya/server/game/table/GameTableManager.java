package com.gogomaya.server.game.table;

import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.specification.GameSpecification;

public interface GameTableManager<State extends GameState> {

    public GameTable<State> poll(GameSpecification specification);

    public void release(GameTable<State> gameTable);

}
