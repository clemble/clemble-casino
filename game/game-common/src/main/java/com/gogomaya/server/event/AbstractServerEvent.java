package com.gogomaya.server.event;

import java.util.Date;

abstract public class AbstractServerEvent implements ServerEvent {

    /**
     * Generated 15/05/13
     */
    private static final long serialVersionUID = 346940113404478382L;

    private Date publishDate = new Date();

    @Override
    public Date getPublishDate() {
        return publishDate;
    }

    public AbstractServerEvent setPublishDate(Date date) {
        this.publishDate = date;
        return this;
    }
}
