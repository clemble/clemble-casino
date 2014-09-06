package com.clemble.casino.integration.game;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.concurrent.atomic.AtomicReference;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.client.game.GameActionOperations;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.RoundGameState;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.game.action.surrender.GiveUpAction;
import com.clemble.casino.game.configuration.GameConfiguration;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.game.event.server.RoundEvent;

public class SimpleRoundGamePlayer<State extends RoundGameState> extends AbstractGamePlayer implements RoundGamePlayer<State> {

    /**
     * Generated 05/07/13
     */
    private static final long serialVersionUID = 694894192572157764L;

    final private AtomicReference<State> state = new AtomicReference<>();
    final private GameActionOperations<State> actionOperations;

    public SimpleRoundGamePlayer(final ClembleCasinoOperations player, final String sessionKey, final GameConfiguration configuration) {
        super(player, sessionKey, configuration);
        this.actionOperations = checkNotNull(player).gameActionOperations(sessionKey);
        this.actionOperations.subscribe(new EventTypeSelector(RoundEvent.class), new EventListener<RoundEvent>() {
            @Override
            public void onEvent(RoundEvent event) {
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
        return getState().getContext().getActionLatch().filterAction(getPlayer());
    }

    @Override
    public void waitForTurn() {
        while (keepAlive.get() && !isToMove() && isAlive())
            waitVersion(getState().getVersion() + 1);
    }

    @Override
    public void perform(GameAction gameAction) {
        GameManagementEvent managementEvent = actionOperations.process(gameAction);
        if (managementEvent instanceof RoundEvent)
            setState((State) ((RoundEvent) managementEvent).getState());
        for (GamePlayer player : dependents) {
            player.syncWith(this);
        }
    }

    final private void setState(State newState) {
        synchronized (versionLock) {
            if (newState != null) {
                if (getState() == null || getState().getVersion() < newState.getVersion()) {
                    System.out.println("updating >> " + getPlayer() + " >> " + getSessionKey() + " >> " + newState.getVersion());
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
    public GamePlayer giveUp() {
        // Step 1. Giving up if needed
        if (isAlive())
            perform(new GiveUpAction(getPlayer()));
        return this;
    }
}
