package com.gogomaya.server.game.action;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.locks.ReentrantLock;

import com.gogomaya.server.game.connection.GameConnection;

public class GameCache<State extends GameState> {

    final private ReentrantLock sessionLock = new ReentrantLock();

    final private GameSession<State> session;

    final private State state;

    final private GameProcessor<State> processor;

    final private GameConnection connection;

    public GameCache(final GameSession<State> session, final State state, final GameProcessor<State> processor, final GameConnection connection) {
        this.session = checkNotNull(session);
        this.state = checkNotNull(state);
        this.processor = checkNotNull(processor);
        this.connection = checkNotNull(connection);
    }

    public ReentrantLock getSessionLock() {
        return sessionLock;
    }

    public GameSession<State> getSession() {
        return session;
    }

    public State getState() {
        return state;
    }

    public GameProcessor<State> getProcessor() {
        return processor;
    }

    public GameConnection getConnection() {
        return connection;
    }

}