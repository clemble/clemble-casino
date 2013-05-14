package com.gogomaya.server.event.impl;

import java.util.Date;

import com.gogomaya.server.event.GogomayaEvent;

abstract public class AbstractGogomayaEvent implements GogomayaEvent {

    /**
     * Generated 15/05/13
     */
    private static final long serialVersionUID = 346940113404478382L;

    private Date publishDate = new Date();

    @Override
    public Date getPublishDate() {
        return publishDate;
    }

    public AbstractGogomayaEvent setPublishDate(Date date) {
        this.publishDate = date;
        return this;
    }
}
