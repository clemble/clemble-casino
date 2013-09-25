package com.gogomaya.server.integration.game;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Closeable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import com.gogomaya.event.ClientEvent;
import com.gogomaya.event.Event;
import com.gogomaya.game.GameSessionKey;
import com.gogomaya.game.GameState;
import com.gogomaya.game.ServerResourse;
import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.game.event.client.GameClientEvent;
import com.gogomaya.game.event.client.surrender.GiveUpEvent;
import com.gogomaya.game.event.server.GameServerEvent;
import com.gogomaya.game.event.server.GameStartedEvent;
import com.gogomaya.game.specification.GameSpecification;
import com.gogomaya.server.integration.player.Player;

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
    private ServerResourse serverResourse;

    public AbstractGameSessionPlayer(final Player player, GameConstruction construction) {
        this.construction = checkNotNull(construction);
        this.player = player;

        player.listen(construction, new GameSessionListener() {
            @Override
            public void notify(Event event) {
                if (event instanceof GameStartedEvent) {
                    GameStartedEvent<?> gameStartedEvent = ((GameStartedEvent<?>) event);
                    session = gameStartedEvent.getSession();
                    serverResourse = gameStartedEvent.getResource();
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
    final public long getPlayerId() {
        return player.getPlayerId();
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
        return getState().getActionLatch().fetchAction(player.getPlayerId());
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
        return !getState().getActionLatch().acted(player.getPlayerId());
    }

    public void giveUp() {
        // Step 1. Giving up if needed
        if (isAlive()) {
            perform(new GiveUpEvent(player.getPlayerId()));
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
        setState(perform(player, serverResourse, session, gameAction));
    }

    abstract public State perform(Player player, ServerResourse resourse, GameSessionKey session, GameClientEvent clientEvent);

}
