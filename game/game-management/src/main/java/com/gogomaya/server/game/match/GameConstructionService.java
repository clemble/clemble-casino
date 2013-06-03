package com.gogomaya.server.game.match;

import java.util.Collection;

import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.specification.GameSpecification;

public interface GameConstructionService<State extends GameState> {

    public GameTable<State> findOpponent(final long playerId, final GameSpecification specification);

    public ScheduledGame schedule(final long initiator, final Collection<Long> playerIds, final GameSpecification specification);

    public ScheduledGame cancel(final long initiator, final long scheduledGameId);

    public ScheduledGame accept(final long playerId, final long scheduledGameId);

    public ScheduledGame decline(final long playerId, final long scheduledGameId);

}
