package com.gogomaya.server.game.cache;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.concurrent.locks.ReentrantLock;

import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.action.GameProcessor;
import com.google.common.collect.ImmutableList;

public class GameCache<State extends GameState> {

    final private ReentrantLock sessionLock = new ReentrantLock();

    final private GameSession<State> session;
    final private GameProcessor<State> processor;
    final private Collection<Long> playerIds;

    public GameCache(final GameSession<State> session, final GameProcessor<State> processor, final Collection<Long> playerIds) {
        this.session = checkNotNull(session);
        this.processor = checkNotNull(processor);
        this.playerIds = ImmutableList.<Long> copyOf(playerIds);
    }

    public ReentrantLock getSessionLock() {
        return sessionLock;
    }

    public GameSession<State> getSession() {
        return session;
    }

    public GameProcessor<State> getProcessor() {
        return processor;
    }

    public Collection<Long> getPlayerIds() {
        return playerIds;
    }

}