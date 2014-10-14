package com.clemble.casino.integration.game;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.lifecycle.management.RoundGameContext;
import com.clemble.casino.game.lifecycle.management.RoundGameState;
import com.clemble.casino.game.lifecycle.management.RoundState;
import com.clemble.casino.game.lifecycle.management.event.GameManagementEvent;
import com.clemble.casino.game.lifecycle.management.event.GamePlayerMovedEvent;
import com.clemble.casino.game.lifecycle.management.event.RoundEndedEvent;
import com.clemble.casino.game.lifecycle.management.outcome.DrawOutcome;
import com.clemble.casino.game.lifecycle.management.outcome.NoOutcome;
import com.clemble.casino.game.lifecycle.management.outcome.PlayerWonOutcome;
import com.clemble.casino.lifecycle.management.event.action.PlayerAction;
import com.clemble.casino.lifecycle.management.event.action.surrender.SurrenderAction;

import java.util.Collection;

/**
 * Created by mavarazy on 14/10/14.
 */
public class NumberState implements RoundState {

    @Override
    public GameManagementEvent process(PlayerAction action, RoundGameState state) {
        RoundGameContext context = state.getContext();
        GameManagementEvent resultEvent = null;
        if (action.getAction() instanceof SelectNumberAction) {
            context.getActionLatch().put(action);
            if (context.getActionLatch().complete()) {
                int maxBet = 0;
                for (PlayerAction<SelectNumberAction> selectNumberEvent : context.getActionLatch().getActions()) {
                    if (selectNumberEvent.getAction().getNumber() > maxBet) {
                        maxBet = selectNumberEvent.getAction().getNumber();
                        resultEvent = RoundEndedEvent.fromContext(state, new PlayerWonOutcome(selectNumberEvent.getPlayer()));
                    } else if (selectNumberEvent.getAction().getNumber() == maxBet) {
                        resultEvent = RoundEndedEvent.fromContext(state, new DrawOutcome());
                    }
                }
            } else {
                resultEvent = new GamePlayerMovedEvent(action.getKey(), action.getPlayer());
            }
        } else if (action.getAction() instanceof SurrenderAction) {
            // Step 1. Fetching player identifier
            String looser = action.getPlayer();
            Collection<String> opponents = context.getPlayerIterator().whoIsOpponents(looser);
            if (opponents.size() == 0) {
                // Step 2. No game started just live the table
                resultEvent = RoundEndedEvent.fromContext(state, new NoOutcome());
            } else {
                String winner = opponents.iterator().next();
                // Step 2. Player gave up, consists of 2 parts - Gave up, and Ended since there is no players involved
                resultEvent = RoundEndedEvent.fromContext(state, new PlayerWonOutcome(winner));
            }
        }
        // Step 3. Sanity check
        if (resultEvent == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayMoveNotSupported);
        // Step 4. Updating version and returning event
        return resultEvent;
    }

    @Override
    public int hashCode() {
        return 29;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof NumberState;
    }

}
