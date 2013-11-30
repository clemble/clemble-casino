package com.clemble.casino.game.construct;

import com.clemble.casino.server.hibernate.AbstractJsonHibernateType;

public class GameRequestHibernate extends AbstractJsonHibernateType<PlayerGameConstructionRequest>{

    public GameRequestHibernate() {
        super(PlayerGameConstructionRequest.class);
    }

}
