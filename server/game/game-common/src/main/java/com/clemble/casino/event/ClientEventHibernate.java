package com.clemble.casino.event;

import com.clemble.casino.server.hibernate.AbstractJsonHibernateType;

public class ClientEventHibernate extends AbstractJsonHibernateType<ClientEvent>{

    public ClientEventHibernate() {
        super(ClientEvent.class);
    }

}
