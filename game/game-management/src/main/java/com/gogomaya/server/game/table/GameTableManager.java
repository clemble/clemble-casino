package com.gogomaya.server.game.table;

import com.gogomaya.server.game.GameSpecification;
import com.gogomaya.server.game.action.GameTable;

public interface GameTableManager<T extends GameTable<?>> {

    public T poll(GameSpecification specification);

    public void setReservable(T gameTable);

}
