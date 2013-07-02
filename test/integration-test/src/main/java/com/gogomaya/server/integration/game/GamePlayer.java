package com.gogomaya.server.integration.game;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.event.Event;
import com.gogomaya.server.game.GameConstuctionAware;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.ServerResourse;
import com.gogomaya.server.game.SessionAware;
import com.gogomaya.server.game.construct.GameConstruction;
import com.gogomaya.server.game.event.server.GameEndedEvent;
import com.gogomaya.server.game.event.server.GameServerEvent;
import com.gogomaya.server.game.event.server.GameStartedEvent;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.integration.player.Player;

abstract public class GamePlayer<State extends GameState> implements SessionAware, GameConstuctionAware {

    final private Player player;

    final private GameSpecification specification;

    final private Object versionLock = new Object();

    final private AtomicBoolean keepAlive = new AtomicBoolean(true);

    final private AtomicReference<State> currentState = new AtomicReference<State>();

    final private GameConstruction construction;

    private Boolean isAlive;

    private ServerResourse serverResourse;

    private long session;

    public GamePlayer(final Player player, final GameConstruction construction) {
        // Step 1. Generic check
        this.player = checkNotNull(player);
        this.specification = checkNotNull(construction.getRequest().getSpecification());
        this.construction = construction;
        // Step 2. Registering listener
        this.player.listen(construction, new GameSessionListener() {
            @Override
            public void notify(Event event) {
                if (event instanceof GameStartedEvent) {
                    GameStartedEvent<State> gameStartedEvent = ((GameStartedEvent<State>) event);
                    session = gameStartedEvent.getSession();
                    setServerResourse(gameStartedEvent.getResource());
                }

                if (event instanceof GameEndedEvent || event instanceof GameStartedEvent) {
                    isAlive = (event instanceof GameStartedEvent);

                    synchronized (versionLock) {
                        versionLock.notifyAll();
                    }
                }

                if (event instanceof GameServerEvent) {
                    setState(((GameServerEvent<State>) event).getState());
                }
            }
        });
    }

    public Boolean isAlive() {
        return isAlive;
    }

    final public Player getPlayer() {
        return player;
    }

    final public GameSpecification getSpecification() {
        return specification;
    }

    final public State getState() {
        return currentState.get();
    }

    final public void setState(State newState) {
        synchronized (versionLock) {
            if (newState != null && this.currentState.get() == null || this.currentState.get().getVersion() < newState.getVersion()) {
                this.currentState.set(newState);
                versionLock.notifyAll();
            }
        }
    }

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
    
    final public void accept(long constuction) {
        
    }

    final public void waitForStart() {
        waitForStart(15_000);
    }

    final public void waitForStart(long timeout) {
        long expirationTime = System.currentTimeMillis() + timeout;
        synchronized (versionLock) {
            while ((isAlive == null || !isAlive) && expirationTime > System.currentTimeMillis()) {
                try {
                    versionLock.wait(timeout);
                } catch (InterruptedException ignore) {
                    throw new RuntimeException(ignore);
                }
            }
        }
        if (isAlive == null || !isAlive)
            throw new RuntimeException();
    }

    final public void waitForTurn() {
        while (keepAlive.get() && !isToMove() && !currentState.get().complete())
            waitVersion(getState().getVersion() + 1);
    }

    final public boolean isToMove() {
        return getState().getNextMove(getPlayer().getPlayerId()) != null;
    }

    final public void syncWith(GamePlayer<State> player) {
        // Step 1. Fetching max available version
        int thisPlayerVersion = player.getState().getVersion();
        int otherPlayerVersion = player.getState().getVersion();
        // Step 2. While versions do not match iterate
        while (keepAlive.get() && thisPlayerVersion != otherPlayerVersion) {
            // Step 2.1 Checking who we need to update
            if (thisPlayerVersion > otherPlayerVersion) {
                player.waitVersion(thisPlayerVersion);
            } else {
                this.waitVersion(otherPlayerVersion);
            }
            // Step 2.2. Updating player version
            thisPlayerVersion = player.getState().getVersion();
            otherPlayerVersion = player.getState().getVersion();
        }
        ;
    }

    @Override
    public long getConstruction() {
        return construction.getConstruction();
    }

    @Override
    public long getSession() {
        return session;
    }

    public void setSession(long session) {
        this.session = session;
    }

    public long getTableId() {
        return serverResourse.getTableId();
    }

    public ServerResourse getServerResourse() {
        return serverResourse;
    }

    public void setServerResourse(ServerResourse serverResourse) {
        this.serverResourse = serverResourse;
    }

    public void clear() {
        player.close();
        keepAlive.set(false);
        if (isAlive != null && isAlive)
            giveUp();
        synchronized (versionLock) {
            versionLock.notifyAll();
        }
    }

    abstract public void giveUp();

}
