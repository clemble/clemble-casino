package com.clemble.casino.integration.game;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Closeable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import com.clemble.casino.ServerRegistry;
import com.clemble.casino.configuration.ResourceLocations;
import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.event.client.GameClientEvent;
import com.clemble.casino.game.event.client.surrender.GiveUpEvent;
import com.clemble.casino.game.event.server.GameServerEvent;
import com.clemble.casino.game.event.server.GameStartedEvent;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.integration.game.GameSessionPlayer;
import com.clemble.casino.integration.player.Player;

abstract public class AbstractGameSessionPlayer<State extends GameState> implements GameSessionPlayer<State>, Closeable {

    /**
     * Generated 04/07/13
     */
    private static final long serialVersionUID = -8412015352988124245L;

    final private Player player;
    final private GameConstruction construction;
    final private Object versionLock = new Object();
    final private AtomicBoolean keepAlive = new AtomicBoolean(true);
    final private AtomicReference<State> currentState = new AtomicReference<State>();

    private GameSessionKey session;

    public AbstractGameSessionPlayer(final Player player, GameConstruction construction) {
        this.construction = checkNotNull(construction);
        this.player = player;

        player.listen(construction, new GameSessionListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void notify(Event event) {
                if (event instanceof GameStartedEvent) {
                    GameStartedEvent<?> gameStartedEvent = ((GameStartedEvent<?>) event);
                    session = gameStartedEvent.getSession();
                }
                if (event instanceof GameServerEvent) {
                    setState(((GameServerEvent<State>) event).getState());
                }
            }
        });
    }

    final public GameConstruction getConstructionInfo() {
        return construction;
    }

    @Override
    final public GameSessionKey getSession() {
        return construction != null ? construction.getSession() : null;
    }

    @Override
    final public GameSpecification getSpecification() {
        return construction.getRequest().getSpecification();
    }

    @Override
    final public Player getPlayer() {
        return player;
    }

    @Override
    final public State getState() {
        return currentState.get();
    }

    final private void setState(State newState) {
        synchronized (versionLock) {
            if (newState != null && this.currentState.get() == null || this.currentState.get().getVersion() < newState.getVersion()) {
                this.currentState.set(newState);
                this.versionLock.notifyAll();
            }
        }
    }

    @Override
    final public boolean isAlive() {
        return keepAlive.get() && currentState.get() != null && currentState.get().getOutcome() == null;
    }

    @Override
    final public int getVersion() {
        return this.currentState.get() == null ? -1 : this.currentState.get().getVersion();
    }

    @Override
    final public void waitVersion(int expectedVersion) {
        if (this.currentState.get() != null && this.currentState.get().getVersion() >= expectedVersion)
            return;

        synchronized (versionLock) {
            while (this.currentState.get() != null && this.currentState.get().getVersion() < expectedVersion) {
                try {
                    versionLock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    final public ClientEvent getNextMove() {
        return getState().getActionLatch().fetchAction(player.getPlayer());
    }

    final public void waitForStart() {
        waitForStart(15_000);
    }

    final public void waitForStart(long timeout) {
        long expirationTime = System.currentTimeMillis() + timeout;
        synchronized (versionLock) {
            while ((keepAlive.get() && currentState.get() == null) && expirationTime > System.currentTimeMillis()) {
                try {
                    versionLock.wait(timeout);
                } catch (InterruptedException ignore) {
                    throw new RuntimeException(ignore);
                }
            }
        }
        if (currentState.get() == null)
            throw new RuntimeException("Game was not started in " + timeout);
    }

    final public void waitForTurn() {
        while (keepAlive.get() && !isToMove() && currentState.get().getOutcome() == null)
            waitVersion(getState().getVersion() + 1);
    }

    final public boolean isToMove() {
        return !getState().getActionLatch().acted(player.getPlayer());
    }

    public void giveUp() {
        // Step 1. Giving up if needed
        if (isAlive()) {
            perform(new GiveUpEvent(player.getPlayer()));
        }
    }

    @Override
    public void close() {
        // Step 1. Giving up
        giveUp();
        // Step 2. Marking for removal
        keepAlive.set(false);
        synchronized (versionLock) {
            versionLock.notifyAll();
        }
    }

    @Override
    public void syncWith(GameSessionPlayer<State> anotherSessionPlayer) {
        // Step 1. While versions do not match iterate
        while (keepAlive.get() && getVersion() != anotherSessionPlayer.getVersion()) {
            int maxVersion = Math.max(getVersion(), anotherSessionPlayer.getVersion());
            // Step 2.1 Checking who we need to update
            anotherSessionPlayer.waitVersion(maxVersion);
            waitVersion(maxVersion);
        }
    }

    @Override
    public void perform(GameClientEvent gameAction) {
        ResourceLocations resourceLocations =  player.getSession().getResourceLocations();
        ServerRegistry gameRegistry = resourceLocations.getServerRegistryConfiguration().getGameRegistry(construction.getSession().getGame());
        setState(perform(player, gameRegistry, session, gameAction));
    }

    abstract public State perform(Player player, ServerRegistry resourse, GameSessionKey session, GameClientEvent clientEvent);

}
