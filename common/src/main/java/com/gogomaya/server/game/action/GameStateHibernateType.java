package com.gogomaya.server.game.action;

import com.gogomaya.server.hibernate.JsonHibernateType;

public class GameStateHibernateType extends JsonHibernateType<GameState<?, ?>> {

    @Override
    @SuppressWarnings("rawtypes")
    public Class<GameState> returnedClass() {
        return GameState.class;
    }

}
