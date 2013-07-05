package com.gogomaya.server.integration.player.listener;

import java.io.Closeable;

public interface PlayerListenerControl extends Closeable {

    @Override
    public void close();

}
