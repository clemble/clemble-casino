package com.gogomaya.server.integration.game;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Closeable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.event.Event;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.ServerResourse;
import com.gogomaya.server.game.construct.GameConstruction;
import com.gogomaya.server.game.event.client.GameClientEvent;
import com.gogomaya.server.game.event.client.GiveUpEvent;
import com.gogomaya.server.game.event.server.GameServerEvent;
import com.gogomaya.server.game.event.server.GameStartedEvent;
import com.gogomaya.server.game.specification.GameSpecification;
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

    private long session;
    private ServerResourse serverResourse;

    public AbstractGameSessionPlayer(final Player player, GameConstruction construction) {
        this.construction = checkNotNull(construction);
        this.player = player;

        player.listen(construction, new GameSessionListener() {
            @Override
            public void notify(Event event) {
                if (event instanceof GameStartedEvent) {
                    GameStartedEvent<State> gameStartedEvent = ((GameStartedEvent<State>) event);
                    session = gameStartedEvent.getSession();
                    serverResourse = gameStartedEvent.getResource();
                }
                if (event instanceof GameServerEvent) {
                    setState(((GameServerEvent<State>) event).getState());
                }
            }
        });
    }

    @Override
    final public long getConstruction() {
        return construction != null ? construction.getConstruction() : 0;
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
        return keepAlive.get() && currentState.get() != null;
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
        return getState().getNextMove(player.getPlayerId());
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
        while (keepAlive.get() && !isToMove() && !currentState.get().complete())
            waitVersion(getState().getVersion() + 1);
    }

    final public boolean isToMove() {
        return getState().getNextMove(player.getPlayerId()) != null;
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
        while (keepAlive.get() && getState().getVersion() != anotherSessionPlayer.getState().getVersion()) {
            int maxVersion = Math.max(getState().getVersion(), anotherSessionPlayer.getState().getVersion());
            // Step 2.1 Checking who we need to update
            anotherSessionPlayer.waitVersion(maxVersion);
            waitVersion(maxVersion);
        }
    }

    @Override
    public void perform(GameClientEvent gameAction) {
        setState(perform(serverResourse, session, gameAction));
    }

    abstract public State perform(ServerResourse resourse, long session, GameClientEvent clientEvent);

}
