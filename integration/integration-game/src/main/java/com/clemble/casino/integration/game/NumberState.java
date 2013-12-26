package com.clemble.casino.integration.game;

import java.util.Collection;

import com.clemble.casino.base.ActionLatch;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.account.GameAccount;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.game.action.surrender.SurrenderAction;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.game.event.server.PlayerMovedEvent;
import com.clemble.casino.game.iterator.GamePlayerIterator;
import com.clemble.casino.game.outcome.DrawOutcome;
import com.clemble.casino.game.outcome.GameOutcome;
import com.clemble.casino.game.outcome.NoOutcome;
import com.clemble.casino.game.outcome.PlayerWonOutcome;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("number")
public class NumberState implements GameState {

    /**
     * Generated 20/12/13
     */
    private static final long serialVersionUID = 6467228372170341563L;

    final private GameContext context;

    private GameOutcome outcome;
    private int version;

    @JsonCreator
    public NumberState(@JsonProperty("context") GameContext context,
            @JsonProperty("outcome") GameOutcome outcome,
            @JsonProperty("version") int version) {
        this.context = context;
        this.context.getActionLatch().expectNext(context.getPlayerIterator().getPlayers(), "selectNumber", SelectNumberAction.class);

        this.outcome = outcome;
        this.version = version;
    }

    @Override
    public <State extends GameState> GameManagementEvent process(GameSession<State> session, GameAction action) {
        // Step 1. Processing Select cell move
        if (outcome != null) {
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayGameEnded);
        }

        GameManagementEvent resultEvent = null;

        if (action instanceof SelectNumberAction) {
            context.getActionLatch().put(action);
            if (context.getActionLatch().complete()) {
                int maxBet = 0;
                for (SelectNumberAction selectNumberEvent : context.getActionLatch().<SelectNumberAction>getActions()) {
                    if (selectNumberEvent.getNumber() > maxBet) {
                        maxBet = selectNumberEvent.getNumber();
                        outcome = new PlayerWonOutcome(selectNumberEvent.getPlayer());
                    } else if (selectNumberEvent.getNumber() == maxBet) {
                        outcome = new DrawOutcome();
                    }
                }
                resultEvent = new GameEndedEvent<>(session, outcome);
            } else {
                resultEvent = new PlayerMovedEvent(session.getSession(), action.getPlayer());
            }
        } else if (action instanceof SurrenderAction) {
            // Step 1. Fetching player identifier
            String looser = ((SurrenderAction) action).getPlayer();
            Collection<String> opponents = context.getPlayerIterator().whoIsOpponents(looser);
            if (opponents.size() == 0 || version == 1) {
                // Step 2. No game started just live the table
                outcome = new NoOutcome();
                resultEvent = new GameEndedEvent<>(session, outcome);
            } else {
                String winner = opponents.iterator().next();
                outcome = new PlayerWonOutcome(winner);
                // Step 2. Player gave up, consists of 2 parts - Gave up, and Ended since there is no players involved
                resultEvent = new GameEndedEvent<>(session, outcome);
            }
        }
        // Step 3. Sanity check
        if (resultEvent == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayMoveNotSupported);
        // Step 4. Updating version and returning event
        version++;
        return resultEvent;
    }

    @Override
    public GameContext getContext() {
        return context;
    }

    @Override
    public GameOutcome getOutcome() {
        return outcome;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NumberState that = (NumberState) o;

        if (version != that.version) return false;
        if (context != null ? !context.equals(that.context) : that.context != null) return false;
        if (outcome != null ? !outcome.equals(that.outcome) : that.outcome != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = context != null ? context.hashCode() : 0;
        result = 31 * result + (outcome != null ? outcome.hashCode() : 0);
        result = 31 * result + version;
        return result;
    }
}
