package com.clemble.casino.server.player.notification;

import java.util.Collection;

public interface RedisSubscribersAware {

    public Collection<String> getChannels();

    public Collection<String> getPatterns();

}
