package com.clemble.casino.server.player.presence;

import java.util.Collection;

public interface RedisSubscribersAware {

    public Collection<String> getChannels();

    public Collection<String> getPatterns();

}
