package com.gogomaya.server.game.active.time;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.action.GameSessionProcessor;
import com.gogomaya.server.game.event.client.MoveTimeoutSurrenderEvent;
import com.gogomaya.server.game.event.client.TotalTimeoutSurrenderEvent;
import com.gogomaya.server.game.rule.time.MoveTimeRule;

public class GameTimeManagementService<State extends GameState> implements Serializable, Runnable {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = 5594629008080772700L;

    final private GameSessionProcessor<State> sessionProcessor;
    final private GameTimeStateFactory<State> timeStateFactory;

    final private PriorityBlockingQueue<GameTimeState> timeStateQueue = new PriorityBlockingQueue<GameTimeState>();
    final private ConcurrentHashMap<Long, GameTimeState> timeStateMap = new ConcurrentHashMap<Long, GameTimeState>();

    public GameTimeManagementService(final GameTimeStateFactory<State> taskFactory, final GameSessionProcessor<State> sessionProcessor) {
        this.timeStateFactory = checkNotNull(taskFactory);
        this.sessionProcessor = checkNotNull(sessionProcessor);
    }

    public GameTimeState fetch(final GameSession<State> session) {
        GameTimeState timeState = timeStateMap.get(session);
        if (timeState == null) {
            timeState = timeStateFactory.construct(session);
            if (timeStateMap.putIfAbsent(timeState.getSession(), timeState) == null)
                timeStateQueue.add(timeState);
        }
        return timeState;
    }

    public void markFinished(GameSession<State> session) {
        timeStateQueue.remove(timeStateMap.remove(session.getSession()));
    }

    public void markStarted(GameSession<State> session, long player) {
        fetch(session).markStarted(player);
    }

    public void markEnded(GameSession<State> session, long player) {

        GameTimeState timeState = fetch(session);
        timeStateQueue.remove(timeState);

        timeState.markEnded(player);

        timeStateQueue.add(timeState);
    }

    @Override
    public void run() {
        Collection<java.util.Map.Entry<Long, ClientEvent>> clientEvents = new ArrayList<java.util.Map.Entry<Long, ClientEvent>>();
        // Step 1. Polling first entry
        Iterator<GameTimeState> timeStateIterator = timeStateQueue.iterator();
        GameTimeState timeState;
        while (timeStateIterator.hasNext() && (timeState = timeStateIterator.next()).breached()) {
            // Step 2. Generating appropriate failure events
            GameTimeBreach gameTimeBreach = timeState.peek();
            switch (gameTimeBreach.getTimeRule().getPunishment()) {
            case loose:
                if (gameTimeBreach.getTimeRule() instanceof MoveTimeRule) {
                    clientEvents.add(new ImmutablePair<Long, ClientEvent>(timeState.getSession(), new MoveTimeoutSurrenderEvent(gameTimeBreach.getPlayerId())));
                } else {
                    clientEvents
                            .add(new ImmutablePair<Long, ClientEvent>(timeState.getSession(), new TotalTimeoutSurrenderEvent(gameTimeBreach.getPlayerId())));
                }
            }
        }
        // Step 3. Invoking events
        for (Entry<Long, ClientEvent> messages : clientEvents) {
            sessionProcessor.process(messages.getKey(), messages.getValue());
        }
    }
}
