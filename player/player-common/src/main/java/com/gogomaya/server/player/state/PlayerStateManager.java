package com.gogomaya.server.player.state;

import java.util.Collection;

import org.springframework.data.redis.connection.MessageListener;

public interface PlayerStateManager {

    public Long isActive(final long player);

    public void markAvailable(final long player);

    public boolean markBusy(final long player, final long sessionId);

    public Collection<Long> markBusy(final Collection<Long> players, final long sessionId);

    public void subscribe(final long player, final MessageListener messageListener);

    public void subscribe(final Collection<Long> players, final MessageListener messageListener);

    public void unsubscribe(final long player, final MessageListener messageListener);
    
    public void unsubscribe(final Collection<Long> players, final MessageListener messageListener);

}
