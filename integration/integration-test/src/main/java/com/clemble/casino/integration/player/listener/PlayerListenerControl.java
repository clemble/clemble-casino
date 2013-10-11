package com.clemble.casino.integration.player.listener;

import java.io.Closeable;

public interface PlayerListenerControl extends Closeable {

    @Override
    public void close();

}
