package com.gogomaya.server.integration.game;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.atomic.AtomicReference;

import com.gogomaya.server.event.GogomayaEvent;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.event.GameEvent;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.integration.game.listener.GameListener;
import com.gogomaya.server.integration.game.listener.GameListenerControl;
import com.gogomaya.server.integration.game.listener.GameListenerOperations;
import com.gogomaya.server.integration.player.Player;

public class GamePlayer<State extends GameState> {

    final private Player player;

    final private GameSpecification specification;

    final private GameTable<State> table;

    final private long sessionId;

    final private Object versionLock = new Object();

    final private AtomicReference<State> currentState = new AtomicReference<State>();

    final private GameListenerControl listenerControl;

    public GamePlayer(final Player player, final GameTable<State> table, final GameListenerOperations<State> listenerOperations) {
        // Step 1. Generic check
        this.player = checkNotNull(player);
        this.table = checkNotNull(table);
        this.specification = checkNotNull(table.getSpecification());
        // Step 2. Specifying current state for the listener
        this.currentState.set(table.getCurrentSession().getState());
        this.sessionId = table.getCurrentSession().getSessionId();
        // Step 3. Registering listener
        checkNotNull(listenerOperations);
        this.listenerControl = listenerOperations.listen(table, new GameListener() {
            @Override @SuppressWarnings("unchecked")
            public void updated(GogomayaEvent event) {
                if (event instanceof GameEvent) {
                    setState(((GameEvent<State>) event).getState());
                }
            }
        });
    }

    final public Player getPlayer() {
        return player;
    }

    final public GameSpecification getSpecification() {
        return specification;
    }

    final public GameTable<State> getTable() {
        return table;
    }

    final public State getState() {
        return currentState.get();
    }

    final public void setState(State newState) {
        synchronized (versionLock) {
            if (this.currentState.get() == null || this.currentState.get().getVersion() < newState.getVersion()) {
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

    final public void waitForUpdate() {
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
        while (thisPlayerVersion != otherPlayerVersion) {
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

    public long getSessionId() {
        return sessionId;
    }

    public long getTableId() {
        return table.getTableId();
    }

    public void clear() {
        listenerControl.stopListener();
    }

}
