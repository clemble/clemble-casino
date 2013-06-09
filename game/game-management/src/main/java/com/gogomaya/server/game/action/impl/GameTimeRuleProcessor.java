package com.gogomaya.server.game.action.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import com.gogomaya.server.game.action.GameCacheService;
import com.gogomaya.server.game.action.GameProcessor;
import com.gogomaya.server.game.action.GameSession;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.game.event.GameEvent;

public class GameTimeRuleProcessor<State extends GameState> extends AbstractGameProcessor<State> {

    final GameCacheService<State> cacheService;

    public GameTimeRuleProcessor(final GameCacheService<State> cacheService, final GameProcessor<State> delegate) {
        super(delegate);
        this.cacheService = checkNotNull(cacheService);
    }

    @Override
    public void beforeMove(GameSession<State> session, GameMove move) {
        move.getPlayerId();
    }

    @Override
    public Collection<GameEvent<State>> afterMove(GameSession<State> session, Collection<GameEvent<State>> events) {
        return null;
    }

}
