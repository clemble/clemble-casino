package com.gogomaya.server.integration.game.listener;

import com.gogomaya.server.game.action.GameTable;

public interface GameListener <T extends GameTable<?>> {

    public void updated(T gameTable);

}
