package com.clemble.casino.integration.game;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.io.Closeable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import com.clemble.casino.ServerRegistry;
import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.client.game.GameActionOperations;
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

abstract public class AbstractGameSessionPlayer<State extends GameState> implements GameSessionPlayer<State>, Closeable {

    /**
     * Generated 04/07/13
     */
    private static final long serialVersionUID = -8412015352988124245L;

    final private ClembleCasinoOperations player;
    final private GameConstruction construction;
    final private GameActionOperations<State> actionOperations; 
    final private Object versionLock = new Object();
    final private AtomicBoolean keepAlive = new AtomicBoolean(true);
    final private AtomicReference<State> currentState = new AtomicReference<State>();

    public AbstractGameSessionPlayer(final ClembleCasinoOperations player, GameConstruction construction) {
        this.construction = checkNotNull(construction);
        this.player = player;
        this.actionOperations = this.player.gameActionOperations(construction.getSession());

        this.actionOperations.subscribe(new EventListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void onEvent(Event event) {
                if (event instanceof GameStartedEvent) {
                    GameStartedEvent<?> gameStartedEvent = ((GameStartedEvent<?>) event);
                }
                if (event instanceof GameServerEvent) {
                    setState(((GameServerEvent<State>) event).getState());
                }
            }
        });
        setState(this.actionOperations.getState());
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
    final public ClembleCasinoOperations getPlayer() {
        return player;
    }

    @Override
    final public State getState() {
        return currentState.get();
    }

    final private void setState(State newState) {
        synchronized (versionLock) {
            if (newState != null) {
                System.out.println("updating >> " + this.player.getPlayer() + " >> " + this.construction.getSession() + " >> " + newState.getVersion());
                if (this.currentState.get() == null || this.currentState.get().getVersion() < newState.getVersion()) {
                    this.currentState.set(newState);
                    this.versionLock.notifyAll();
                }
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
            throw new RuntimeException(construction.getSession() + " was not started after " + timeout);
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
        setState((State) player.gameActionOperations(construction.getSession()).process(gameAction));
    }

    abstract public State perform(ClembleCasinoOperations player, ServerRegistry resourse, GameSessionKey session, GameClientEvent clientEvent);

}
