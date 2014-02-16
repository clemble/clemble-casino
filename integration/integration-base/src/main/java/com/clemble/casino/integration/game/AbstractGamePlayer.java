package com.clemble.casino.integration.game;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.client.event.EventSelector;
import com.clemble.casino.client.event.EventSelectors;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.client.event.GameSessionEventSelector;
import com.clemble.casino.game.GameSessionAwareEvent;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.game.outcome.GameOutcome;
import com.clemble.casino.game.specification.GameConfigurationKey;
import com.clemble.casino.integration.event.EventAccumulator;

abstract public class AbstractGamePlayer implements GamePlayer {

    /**
     * Generated 04/07/13
     */
    private static final long serialVersionUID = -8412015352988124245L;

    final private Collection<GamePlayer> dependents = new LinkedBlockingQueue<GamePlayer>();

    final private ClembleCasinoOperations player;
    final private GameConstruction construction;
    final private Object versionLock = new Object();
    final private EventAccumulator<GameSessionAwareEvent> eventAccumulator = new EventAccumulator<GameSessionAwareEvent>();

    final private AtomicBoolean keepAlive = new AtomicBoolean(true);
    final private AtomicReference<GameOutcome> outcome = new AtomicReference<>();

    public AbstractGamePlayer(final ClembleCasinoOperations player, GameConstruction construction) {
        this.construction = checkNotNull(construction);
        this.player = player;
        // Step 1. Listening for outcomes
        EventSelector endEventSelector = EventSelectors.where(new GameSessionEventSelector(construction.getSession()))
            .and(new EventTypeSelector(GameEndedEvent.class));
        this.player.listenerOperations().subscribe(endEventSelector, new EventListener<GameEndedEvent<?>>() {
            @Override
            public void onEvent(GameEndedEvent<?> event) {
                outcome.set(event.getOutcome());
            }
        });
    }

    @Override
    public String getPlayer() {
        return player.getPlayer();
    }

    @Override
    final public GameConstruction getConstructionInfo() {
        return construction;
    }

    @Override
    final public GameSessionKey getSession() {
        return construction != null ? construction.getSession() : null;
    }

    @Override
    final public GameConfigurationKey getConfigurationKey() {
        return construction.getRequest().getConfiguration().getConfigurationKey();
    }

    @Override
    final public ClembleCasinoOperations playerOperations() {
        return player;
    }

    @Override
    final public boolean isAlive() {
        return keepAlive.get() && outcome.get() == null && getVersion() > 0;
    }

    @Override
    final public void waitVersion(int expectedVersion) {
        if (getVersion() >= expectedVersion)
            return;

        synchronized (versionLock) {
            while (isAlive() && getVersion() < expectedVersion) {
                try {
                    versionLock.wait(15000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public List<GameSessionAwareEvent> getEvents() {
        return eventAccumulator.toList();
    }

    @Override
    final public void waitForEnd() {
        long expirationTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(15);
        synchronized (versionLock) {
            while (isAlive() && expirationTime > System.currentTimeMillis()) {
                try {
                    versionLock.wait(TimeUnit.SECONDS.toMillis(1));
                } catch (InterruptedException e) {
                }
            }
        }
    }

    @Override
    final public void waitForStart() {
        waitForStart(15_000);
    }

    @Override
    final public void waitForStart(long timeout) {
        long expirationTime = timeout > 0 ? System.currentTimeMillis() + timeout : Long.MAX_VALUE;
        synchronized (versionLock) {
            while (keepAlive.get() && getVersion() == 0 && expirationTime > System.currentTimeMillis()) {
                try {
                    if (timeout > 0) {
                        versionLock.wait(timeout);
                    } else {
                        versionLock.wait();
                    }
                } catch (InterruptedException ignore) {
                    throw new RuntimeException(ignore);
                }
            }
        }
        if (getVersion() == 0)
            throw new RuntimeException(player.getPlayer() + " " + construction.getSession() + " was not started after " + timeout);
    }

    @Override
    public void close() {
        // Step 1. Giving up
        giveUp();
        // Step 2. Marking for removal
        keepAlive.set(false);
        // Step 3. Releasing the lock
        synchronized (versionLock) {
            versionLock.notifyAll();
        }
    }

    @Override
    public void syncWith(GamePlayer anotherSessionPlayer) {
        if(!(anotherSessionPlayer instanceof MatchGamePlayer))
            throw new IllegalArgumentException();
        // Step 1. While versions do not match iterate
        while (keepAlive.get() && getVersion() != anotherSessionPlayer.getVersion()) {
            int maxVersion = Math.max(getVersion(), anotherSessionPlayer.getVersion());
            // Step 2.1 Checking who we need to update
            anotherSessionPlayer.waitVersion(maxVersion);
            waitVersion(maxVersion);
        }
    }

    @Override
    public GameOutcome getOutcome() {
        return outcome.get();
    }

    public void addDependent(GamePlayer player) {
        if (player != null && player != this) {
            dependents.add((GamePlayer) player);
        }
    }

    @Override
    public void addDependent(Collection<? extends GamePlayer> players) {
        for (GamePlayer player : players)
            addDependent(player);
    }

}
