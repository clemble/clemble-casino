package com.clemble.casino.integration.game;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.io.Closeable;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import com.clemble.casino.ServerRegistry;
import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.client.game.GameActionOperations;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameSessionAwareEvent;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.game.action.surrender.GiveUpAction;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.game.event.server.GameStateManagementEvent;
import com.clemble.casino.game.outcome.GameOutcome;
import com.clemble.casino.game.specification.GameConfigurationKey;
import com.clemble.casino.integration.event.EventAccumulator;

abstract public class AbstractGameSessionPlayer<State extends GameState> implements GameSessionPlayer<State>, EventListener<GameSessionAwareEvent>, Closeable {

    /**
     * Generated 04/07/13
     */
    private static final long serialVersionUID = -8412015352988124245L;

    final private Collection<GameSessionPlayer<State>> dependents = new LinkedBlockingQueue<GameSessionPlayer<State>>();

    final private ClembleCasinoOperations player;
    final private GameConstruction construction;
    final private GameActionOperations<State> actionOperations;
    final private Object versionLock = new Object();
    final private EventAccumulator<GameSessionAwareEvent> eventAccumulator = new EventAccumulator<GameSessionAwareEvent>();
    final private AtomicBoolean keepAlive = new AtomicBoolean(true);
    final private AtomicReference<State> currentState = new AtomicReference<>();
    final private AtomicReference<GameOutcome> outcome = new AtomicReference<>();

    public AbstractGameSessionPlayer(final ClembleCasinoOperations player, GameConstruction construction) {
        this.construction = checkNotNull(construction);
        this.player = player;
        this.actionOperations = this.player.gameActionOperations(construction.getSession());

        this.actionOperations.subscribe(this);
        this.actionOperations.subscribe(eventAccumulator);
        setState(this.actionOperations.getState());
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
    final public State getState() {
        return currentState.get();
    }

    @Override
    public void onEvent(GameSessionAwareEvent event) {
        if (event instanceof GameEndedEvent<?>)
            outcome.set(((GameEndedEvent<?>) event).getOutcome());
        if (event instanceof GameStateManagementEvent)
            setState(((GameStateManagementEvent<State>) event).getState());
    }

    final private void setState(State newState) {
        synchronized (versionLock) {
            if (newState != null) {
                if (getState() == null || getState().getVersion() < newState.getVersion()) {
                    System.out.println("updating >> " + this.player.getPlayer() + " >> " + this.construction.getSession() + " >> " + newState.getVersion());
                    this.currentState.set(newState);
                    this.versionLock.notifyAll();
                }
            }
        }
    }

    @Override
    final public boolean isAlive() {
        return keepAlive.get() && outcome.get() == null && getState() != null;
    }

    @Override
    final public int getVersion() {
        return getState() == null ? -1 : getState().getVersion();
    }

    @Override
    final public void waitVersion(int expectedVersion) {
        if (getState() != null && getState().getVersion() >= expectedVersion)
            return;

        if (getState() == null)
            setState(actionOperations.getState());

        synchronized (versionLock) {
            while (isAlive() && getState().getVersion() < expectedVersion) {
                try {
                    versionLock.wait(15000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    final public Event getNextMove() {
        return getState().getContext().getActionLatch().fetchAction(player.getPlayer());
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
        if (getState() == null)
            setState(this.actionOperations.getState());
        synchronized (versionLock) {
            while (keepAlive.get() && getState() == null && expirationTime > System.currentTimeMillis()) {
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
        if (getState() == null)
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
        // Step 3. Releasing the lock
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
        GameManagementEvent managementEvent = player.gameActionOperations(construction.getSession()).process(gameAction);
        onEvent(managementEvent);
        for (GameSessionPlayer<State> player : dependents)
            player.syncWith(this);
    }

    abstract public State perform(ClembleCasinoOperations player, ServerRegistry resourse, GameSessionKey session, GameAction clientEvent);

    @Override
    public GameOutcome getOutcome() {
        return outcome.get();
    }

    @Override
    public void addDependent(GameSessionPlayer<State> player) {
        if (player != null && player != this)
            dependents.add(player);
    }

    @Override
    public void addDependent(Collection<GameSessionPlayer<State>> players) {
        for (GameSessionPlayer<State> player : players)
            addDependent(player);
    }

}
