package com.clemble.casino.event;

import com.clemble.casino.game.event.client.GameAction;
import com.clemble.casino.server.hibernate.AbstractJsonHibernateType;

public class ClientEventHibernate extends AbstractJsonHibernateType<GameAction>{

    public ClientEventHibernate() {
        super(GameAction.class);
    }

}
