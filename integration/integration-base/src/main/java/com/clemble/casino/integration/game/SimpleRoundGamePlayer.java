package com.clemble.casino.integration.game;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.concurrent.atomic.AtomicReference;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.client.game.GameActionOperations;
import com.clemble.casino.event.Event;
import com.clemble.casino.lifecycle.management.event.action.Action;
import com.clemble.casino.lifecycle.management.event.action.surrender.GiveUpAction;
import com.clemble.casino.game.lifecycle.management.RoundGameState;
import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;
import com.clemble.casino.game.lifecycle.management.event.GameManagementEvent;
import com.clemble.casino.game.lifecycle.management.event.RoundEvent;

public class SimpleRoundGamePlayer extends AbstractGamePlayer implements RoundGamePlayer {

    /**
     * Generated 05/07/13
     */
    private static final long serialVersionUID = 694894192572157764L;

    final private AtomicReference<RoundGameState> state = new AtomicReference<>();
    final private GameActionOperations<RoundGameState> actionOperations;

    public SimpleRoundGamePlayer(final ClembleCasinoOperations player, final String sessionKey, final GameConfiguration configuration) {
        super(player, sessionKey, configuration);
        this.actionOperations = checkNotNull(player).gameActionOperations(sessionKey);
        this.actionOperations.subscribe(new EventTypeSelector(RoundEvent.class), new EventListener<RoundEvent>() {
            @Override
            public void onEvent(RoundEvent event) {
                setState(event.getState());
            }
        });
        this.setState(this.actionOperations.getState());
    }

    @Override
    public RoundGameState getState() {
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
    public void perform(Action gameAction) {
        LOG.debug("performing {}", gameAction);
        GameManagementEvent managementEvent = actionOperations.process(gameAction);
        if (managementEvent instanceof RoundEvent)
            setState(((RoundEvent) managementEvent).getState());
        for (GamePlayer player : dependents) {
            player.syncWith(this);
        }
    }

    final private void setState(RoundGameState newState) {
        synchronized (versionLock) {
            if (newState != null) {
                if (getState() == null || getState().getVersion() < newState.getVersion()) {
                    LOG.info("updating state to {}", newState.getVersion());
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
        if (isAlive()) {
            perform(new GiveUpAction());
        } else {
            LOG.debug("can't give up player is not ALIVE");
        }
        return this;
    }
}
