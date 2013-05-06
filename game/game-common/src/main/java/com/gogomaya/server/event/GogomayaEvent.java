package com.gogomaya.server.event;

import java.io.Serializable;
import java.util.Date;

public interface GogomayaEvent extends Serializable {

    public Date getPublishDate();

}
