package com.clemble.casino.integration.game;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.concurrent.atomic.AtomicReference;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.client.game.GameActionOperations;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.game.action.surrender.GiveUpAction;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.game.event.server.GameMatchEvent;

public class SimpleMatchGamePlayer<State extends GameState> extends AbstractGamePlayer implements MatchGamePlayer<State> {

    /**
     * Generated 05/07/13
     */
    private static final long serialVersionUID = 694894192572157764L;

    final private AtomicReference<State> state = new AtomicReference<>();
    final private GameActionOperations<State> actionOperations;

    public SimpleMatchGamePlayer(final ClembleCasinoOperations player, final GameConstruction construction, final GameActionOperations<State> gameEngineController) {
        super(player, construction);
        this.actionOperations = checkNotNull(gameEngineController);
        this.actionOperations.subscribe(new EventTypeSelector(GameMatchEvent.class), new EventListener<GameMatchEvent>() {
            @Override
            public void onEvent(GameMatchEvent event) {
                setState((State) event.getState());
            }
        });
        this.setState(this.actionOperations.getState());
    }

    @Override
    public State getState() {
        return state.get();
    }

    @Override
    public boolean isToMove() {
        return getState() != null && !getState().getContext().getActionLatch().acted(getPlayer());
    }

    @Override
    public Event getNextMove() {
        return getState().getContext().getActionLatch().fetchAction(getPlayer());
    }

    @Override
    public void waitForTurn() {
        while (keepAlive.get() && !isToMove() && isAlive())
            waitVersion(getState().getVersion() + 1);
    }

    @Override
    public void perform(GameAction gameAction) {
        GameManagementEvent managementEvent = actionOperations.process(gameAction);
        if (managementEvent instanceof GameMatchEvent)
            setState((State) ((GameMatchEvent) managementEvent).getState());
        for (GamePlayer player : dependents) {
            player.syncWith(this);
        }
    }

    final private void setState(State newState) {
        synchronized (versionLock) {
            if (newState != null) {
                if (getState() == null || getState().getVersion() < newState.getVersion()) {
                    System.out.println("updating >> " + getPlayer() + " >> " + getSession() + " >> " + newState.getVersion());
                    this.state.set(newState);
                    this.versionLock.notifyAll();
                }
            }
        }
    }


    @Override
    public int getVersion() {
        return state.get() != null ? state.get().getVersion() : -1;
    }

    @Override
    public void giveUp() {
        // Step 1. Giving up if needed
        if (isAlive())
            perform(new GiveUpAction(getPlayer()));
    }
}
