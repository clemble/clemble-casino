package com.gogomaya.server.player.state;

import java.util.Collection;
import java.util.Date;

public interface PlayerStateManager {

    public boolean isAvailable(final long player);

    public boolean areAvailable(final Collection<Long> players);

    public void markAvailable(final long player);

    public Date refresh(final long player);

    public Date markAlive(final long player);

    public void markLeft(final long player);

    public Long getActiveSession(final long player);

    public boolean markBusy(final long player, final long sessionId);

    public boolean markBusy(final Collection<Long> players, final long sessionId);

    public void subscribe(final long player, final PlayerStateListener messageListener);

    public void subscribe(final Collection<Long> players, final PlayerStateListener messageListener);

    public void unsubscribe(final long player, final PlayerStateListener messageListener);

    public void unsubscribe(final Collection<Long> players, final PlayerStateListener messageListener);

}
