package com.clemble.casino.server.game.construction;

import com.clemble.casino.game.construction.PlayerGameConstructionRequest;
import com.clemble.casino.server.hibernate.AbstractJsonHibernateType;

public class GameRequestHibernate extends AbstractJsonHibernateType<PlayerGameConstructionRequest>{

    public GameRequestHibernate() {
        super(PlayerGameConstructionRequest.class);
    }

}
