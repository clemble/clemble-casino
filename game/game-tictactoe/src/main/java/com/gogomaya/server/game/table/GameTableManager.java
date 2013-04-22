package com.gogomaya.server.game.table;

import com.gogomaya.server.game.GameSpecification;
import com.gogomaya.server.game.action.GameTable;

public interface GameTableManager {

    public GameTable<?> poll(GameSpecification specification);

    public void setReservable(GameTable<?> gameTable);

}
