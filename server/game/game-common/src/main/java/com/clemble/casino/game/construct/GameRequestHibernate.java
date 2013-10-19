package com.clemble.casino.game.construct;

import com.clemble.casino.server.hibernate.AbstractJsonHibernateType;

public class GameRequestHibernate extends AbstractJsonHibernateType<GameRequest>{

    public GameRequestHibernate() {
        super(GameRequest.class);
    }

}
