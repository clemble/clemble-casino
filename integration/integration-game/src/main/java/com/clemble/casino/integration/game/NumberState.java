package com.clemble.casino.integration.game;

import java.util.Collection;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.event.Event;
import com.clemble.casino.event.PlayerAwareEvent;
import com.clemble.casino.event.surrender.SurrenderEvent;
import com.clemble.casino.game.RoundGameContext;
import com.clemble.casino.game.RoundGameState;
import com.clemble.casino.game.action.ClientGameEvent;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.game.event.server.RoundEndedEvent;
import com.clemble.casino.game.event.server.PlayerMovedEvent;
import com.clemble.casino.game.outcome.DrawOutcome;
import com.clemble.casino.game.outcome.NoOutcome;
import com.clemble.casino.game.outcome.PlayerWonOutcome;
import com.clemble.casino.game.unit.GameUnit;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("number")
public class NumberState implements RoundGameState {

    /**
     * Generated 20/12/13
     */
    private static final long serialVersionUID = 6467228372170341563L;

    final private RoundGameContext context;
    final private GameUnit state;
    private int version;

    @JsonCreator
    public NumberState(@JsonProperty("context") RoundGameContext context,
            @JsonProperty("state") GameUnit state,
            @JsonProperty("version") int version) {
        this.context = context;
        this.state = state;
        this.context.getActionLatch().expectNext(context.getPlayerIterator().getPlayers(), SelectNumberEvent.class);

        this.version = version;
    }

    @Override
    public GameUnit getState() {
        return state;
    }

    @Override
    public GameManagementEvent process(PlayerAwareEvent action) {
        // Step 1. Processing Select cell move
        GameManagementEvent resultEvent = null;

        if (action instanceof SelectNumberEvent) {
            context.getActionLatch().put(action);
            if (context.getActionLatch().complete()) {
                int maxBet = 0;
                for (SelectNumberEvent selectNumberEvent : context.getActionLatch().<SelectNumberEvent>getActions()) {
                    if (selectNumberEvent.getNumber() > maxBet) {
                        maxBet = selectNumberEvent.getNumber();
                        resultEvent = new RoundEndedEvent(context.getSessionKey(), this, new PlayerWonOutcome(selectNumberEvent.getPlayer()), context);
                    } else if (selectNumberEvent.getNumber() == maxBet) {
                        resultEvent = new RoundEndedEvent(context.getSessionKey(), this, new DrawOutcome(), context);
                    }
                }
            } else {
                resultEvent = new PlayerMovedEvent(context.getSessionKey(), action.getPlayer());
            }
        } else if (action instanceof SurrenderEvent) {
            // Step 1. Fetching player identifier
            String looser = ((SurrenderEvent) action).getPlayer();
            Collection<String> opponents = context.getPlayerIterator().whoIsOpponents(looser);
            if (opponents.size() == 0 || version == 1) {
                // Step 2. No game started just live the table
                resultEvent = RoundEndedEvent.fromContext(context, this, new NoOutcome());
            } else {
                String winner = opponents.iterator().next();
                // Step 2. Player gave up, consists of 2 parts - Gave up, and Ended since there is no players involved
                resultEvent = RoundEndedEvent.fromContext(context, this, new PlayerWonOutcome(winner));
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
    public RoundGameContext getContext() {
        return context;
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

        return true;
    }

    @Override
    public int hashCode() {
        int result = context != null ? context.hashCode() : 0;
        result = 31 * result + version;
        return result;
    }
}
