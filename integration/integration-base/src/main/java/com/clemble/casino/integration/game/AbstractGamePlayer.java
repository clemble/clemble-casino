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
import com.clemble.casino.lifecycle.management.outcome.Outcome;
import com.clemble.casino.game.lifecycle.record.GameRecord;
import com.clemble.casino.game.lifecycle.construction.GameConstruction;
import com.clemble.casino.game.lifecycle.management.event.GameEndedEvent;
import com.clemble.casino.game.event.GameEvent;
import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;
import com.clemble.casino.integration.event.EventAccumulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class AbstractGamePlayer implements GamePlayer {

    /**
     * Generated 04/07/13
     */
    private static final long serialVersionUID = -8412015352988124245L;

    final protected Logger LOG;

    //TODO make an external concern
    final protected Collection<GamePlayer> dependents = new LinkedBlockingQueue<GamePlayer>();

    final protected Object versionLock = new Object();

    final private String sessionKey;
    final private GameConfiguration configuration;
    final private ClembleCasinoOperations player;
    final private EventAccumulator<GameEvent> eventAccumulator;

    final protected AtomicBoolean keepAlive = new AtomicBoolean(true);
    final private AtomicReference<Outcome> outcome = new AtomicReference<>();
    

    public AbstractGamePlayer(final ClembleCasinoOperations player, final GameConstruction construction) {
        this(player, construction.getSessionKey(), construction.getConfiguration());
    }

    public AbstractGamePlayer(final ClembleCasinoOperations player, final String sessionKey, final GameConfiguration configuration) {
        this.LOG = LoggerFactory.getLogger("GP > " + sessionKey + " > " + player.getPlayer());
        this.player = checkNotNull(player);
        this.sessionKey = checkNotNull(sessionKey);
        this.configuration = checkNotNull(configuration);
        // Step 1. Listening for outcomes
        EventSelector endEventSelector = EventSelectors.where(new GameSessionEventSelector(sessionKey))
            .and(new EventTypeSelector(GameEndedEvent.class));
        this.player.listenerOperations().subscribe(endEventSelector, new EventListener<GameEndedEvent>() {
            @Override
            public void onEvent(GameEndedEvent event) {
                outcome.set(event.getOutcome());
                synchronized (versionLock) {
                    versionLock.notifyAll();
                }
            }
        });
        // Step 2. Listening for all possible events
        eventAccumulator = new EventAccumulator<GameEvent>();
        EventSelector eventSelector = new GameSessionEventSelector(sessionKey);
        this.player.listenerOperations().subscribe(eventSelector, eventAccumulator);
    }

    @Override
    final public String getPlayer() {
        return player.getPlayer();
    }

    @Override
    final public String getSessionKey() {
        return sessionKey;
    }

    @Override
    final public GameConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    final public ClembleCasinoOperations playerOperations() {
        return player;
    }

    @Override
    final public boolean isAlive() {
        return keepAlive.get() && outcome.get() == null && getVersion() > -1;
    }

    @Override
    final public GamePlayer waitVersion(int expectedVersion) {
        LOG.debug("waiting for version {} current version", expectedVersion, getVersion());
        if (getVersion() >= expectedVersion)
            return this;

        synchronized (versionLock) {
            while (keepAlive.get() && getVersion() < expectedVersion) {
                try {
                    versionLock.wait(15000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return this;
    }

    @Override
    final public List<GameEvent> getEvents() {
        return eventAccumulator.toList();
    }

    @Override
    final public GamePlayer waitForEnd() {
        return waitForEnd(TimeUnit.SECONDS.toMillis(15));
    }

    @Override
    final public GamePlayer waitForEnd(long timeout) {
        long expirationTime = System.currentTimeMillis() + timeout;
        synchronized (versionLock) {
            while (isAlive() && expirationTime > System.currentTimeMillis()) {
                try {
                    versionLock.wait(TimeUnit.SECONDS.toMillis(1));
                } catch (InterruptedException e) {
                }
            }
        }
        if(isAlive())
            throw new RuntimeException(player.getPlayer() + " " + sessionKey + " did not end after " + timeout);
        return this;
    }

    @Override
    final public GamePlayer waitForStart() {
        return waitForStart(15_000);
    }

    @Override
    final public GamePlayer waitForStart(long timeout) {
        LOG.debug("wait for start");
        long expirationTime = timeout > 0 ? System.currentTimeMillis() + timeout : Long.MAX_VALUE;
        synchronized (versionLock) {
            while (keepAlive.get() && getVersion() < 0 && expirationTime > System.currentTimeMillis()) {
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
        if (getVersion() < 0) {
            LOG.debug("start failed");
            throw new RuntimeException(player.getPlayer() + " " + sessionKey + " was not started after " + timeout);
        } else {
            LOG.debug("game started");
        }
        return this;
    }

    @Override
    final public void close() {
        LOG.debug("closing player");
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
    final public GamePlayer syncWith(GamePlayer anotherSessionPlayer) {
        if(!(anotherSessionPlayer instanceof RoundGamePlayer))
            throw new IllegalArgumentException();
        // Step 1. While versions do not match iterate
        while (keepAlive.get() && getVersion() != anotherSessionPlayer.getVersion()) {
            int maxVersion = Math.max(getVersion(), anotherSessionPlayer.getVersion());
            // Step 2.1 Checking who we need to update
            anotherSessionPlayer.waitVersion(maxVersion);
            waitVersion(maxVersion);
        }
        return this;
    }

    @Override
    final public Outcome getOutcome() {
        return outcome.get();
    }

    final public GamePlayer addDependent(GamePlayer player) {
        if (player != null && player != this)
            dependents.add(player);
        return this;
    }

    @Override
    final public GamePlayer addDependent(Collection<? extends GamePlayer> players) {
        for (GamePlayer player : players)
            addDependent(player);
        return this;
    }

    @Override
    final public GameRecord getRecord(){
        return player.gameRecordOperations().get(sessionKey);
    }

}
