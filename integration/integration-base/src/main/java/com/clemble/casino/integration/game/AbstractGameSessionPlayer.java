package com.clemble.casino.integration.game;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.io.Closeable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import com.clemble.casino.ServerRegistry;
import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.client.game.GameActionOperations;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.game.action.surrender.GiveUpAction;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.game.event.server.GameStateManagementEvent;
import com.clemble.casino.game.outcome.GameOutcome;
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
    final private AtomicReference<GameOutcome> isAlive = new AtomicReference<>();

    public AbstractGameSessionPlayer(final ClembleCasinoOperations player, GameConstruction construction) {
        this.construction = checkNotNull(construction);
        this.player = player;
        this.actionOperations = this.player.gameActionOperations(construction.getSession());

        this.actionOperations.subscribe(new EventListener<GameManagementEvent>() {
            @Override
            @SuppressWarnings("unchecked")
            public void onEvent(GameManagementEvent event) {
                if (event instanceof GameEndedEvent<?>)
                    isAlive.set(((GameEndedEvent<?>) event).getOutcome());
                if (event instanceof GameStateManagementEvent)
                    setState(((GameStateManagementEvent<State>) event).getState());
            }
        });
        setState(this.actionOperations.getState());
    }

    @Override
    public String getPlayer(){
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
    final public GameSpecification getSpecification() {
        return construction.getRequest().getSpecification();
    }

    @Override
    final public ClembleCasinoOperations playerOperations() {
        return player;
    }

    @Override
    final public State getState() {
        return currentState.get();
    }

    final private void setState(State newState) {
        synchronized (versionLock) {
            if (newState != null) {
                if (this.currentState.get() == null || this.currentState.get().getVersion() < newState.getVersion()) {
                    System.out.println("updating >> " + this.player.getPlayer() + " >> " + this.construction.getSession() + " >> " + newState.getVersion());
                    this.currentState.set(newState);
                    this.versionLock.notifyAll();
                }
            }
        }
    }

    @Override
    final public boolean isAlive() {
        return keepAlive.get() && isAlive.get() == null && currentState.get() != null;
    }

    @Override
    final public int getVersion() {
        return this.currentState.get() == null ? -1 : this.currentState.get().getVersion();
    }

    @Override
    final public void waitVersion(int expectedVersion) {
        if (this.currentState.get() != null && this.currentState.get().getVersion() >= expectedVersion)
            return;

        if(this.currentState.get() == null)
            setState(actionOperations.getState());

        synchronized (versionLock) {
            while (this.currentState.get() != null && this.currentState.get().getVersion() < expectedVersion) {
                try {
                    versionLock.wait(15000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                setState(actionOperations.getState());
            }
        }
    }

    @Override
    final public Event getNextMove() {
        return getState().getContext().getActionLatch().fetchAction(player.getPlayer());
    }

    @Override
    final public void waitForEnd() {
        long expirationTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(15);
        synchronized (versionLock) {
            while(isAlive.get() == null && expirationTime > System.currentTimeMillis()) {
                try {
                    versionLock.wait(TimeUnit.SECONDS.toMillis(15));
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
        if (currentState.get() == null)
            setState(this.actionOperations.getState());
        synchronized (versionLock) {
            while ((keepAlive.get() && currentState.get() == null) && expirationTime > System.currentTimeMillis()) {
                try {
                    if(timeout > 0) {
                        versionLock.wait(timeout);
                    } else {
                        versionLock.wait();
                    }
                } catch (InterruptedException ignore) {
                    throw new RuntimeException(ignore);
                }
            }
        }
        if (currentState.get() == null)
            throw new RuntimeException(player.getPlayer() + " " + construction.getSession() + " was not started after " + timeout);
    }

    @Override
    final public void waitForTurn() {
        while (keepAlive.get() && !isToMove() && isAlive())
            waitVersion(getState().getVersion() + 1);
    }

    @Override
    final public boolean isToMove() {
        return !getState().getContext().getActionLatch().acted(player.getPlayer());
    }

    @Override
    public void giveUp() {
        // Step 1. Giving up if needed
        if (isAlive()) {
            perform(new GiveUpAction(player.getPlayer()));
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
    public void perform(GameAction gameAction) {
        setState((State) player.gameActionOperations(construction.getSession()).process(gameAction));
    }

    abstract public State perform(ClembleCasinoOperations player, ServerRegistry resourse, GameSessionKey session, GameAction clientEvent);

    @Override
    public GameOutcome getOutcome(){
        return isAlive.get();
    }

}
