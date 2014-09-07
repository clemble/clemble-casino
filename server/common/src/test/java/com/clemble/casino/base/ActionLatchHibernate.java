package com.clemble.casino.base;

import com.clemble.casino.ActionLatch;
import com.clemble.casino.server.hibernate.AbstractJsonHibernateType;

public class ActionLatchHibernate extends AbstractJsonHibernateType<ActionLatch> {

    public ActionLatchHibernate() {
        super(ActionLatch.class);
    }

}
