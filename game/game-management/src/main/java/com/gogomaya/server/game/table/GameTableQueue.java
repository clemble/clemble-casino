package com.gogomaya.server.game.table;

import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.specification.GameSpecification;

public interface GameTableQueue<State extends GameState> {

    public Long poll(GameSpecification specification);

    public void add(GameTable<State> gameTable);

}
