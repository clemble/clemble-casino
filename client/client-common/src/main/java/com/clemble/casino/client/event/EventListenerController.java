package com.clemble.casino.client.event;

import java.io.Closeable;

public interface EventListenerController extends Closeable {

    public void close();

}
