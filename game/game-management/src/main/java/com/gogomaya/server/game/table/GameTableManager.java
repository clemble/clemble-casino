package com.gogomaya.server.game.table;

import com.gogomaya.server.game.GameSpecification;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameTable;

public interface GameTableManager<State extends GameState> {

    public GameTable<State> poll(GameSpecification specification);

    public void setReservable(GameTable<State> gameTable);

}
