package com.clemble.casino.server.game.construction;

import com.clemble.casino.game.construct.PlayerGameConstructionRequest;
import com.clemble.casino.server.hibernate.AbstractJsonHibernateType;

public class GameRequestHibernate extends AbstractJsonHibernateType<PlayerGameConstructionRequest>{

    public GameRequestHibernate() {
        super(PlayerGameConstructionRequest.class);
    }

}
