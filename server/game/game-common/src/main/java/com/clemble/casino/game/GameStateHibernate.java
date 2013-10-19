package com.clemble.casino.game;

import com.clemble.casino.server.hibernate.AbstractJsonHibernateType;

public class GameStateHibernate extends AbstractJsonHibernateType<GameState> {

    public GameStateHibernate() {
        super(GameState.class);
    }

}
