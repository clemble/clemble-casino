package com.gogomaya.server.game.active.time;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.SessionAware;
import com.gogomaya.server.game.action.GameSessionProcessor;
import com.gogomaya.server.game.event.client.MoveTimeoutSurrenderEvent;
import com.gogomaya.server.game.event.client.TotalTimeoutSurrenderEvent;
import com.gogomaya.server.game.rule.time.MoveTimeRule;

public class GameTimeManagementService<State extends GameState> implements Serializable {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = 5594629008080772700L;

    final private GameSessionProcessor<State> sessionProcessor;
    final private GameTimeStateFactory timeStateFactory;
    final private ScheduledExecutorService executorService;

    final private PriorityBlockingQueue<GameTimeState> timeStateQueue = new PriorityBlockingQueue<GameTimeState>();
    final private ConcurrentHashMap<Long, GameTimeState> timeStateMap = new ConcurrentHashMap<Long, GameTimeState>();

    public GameTimeManagementService(final GameTimeStateFactory timeStateFactory, final GameSessionProcessor<State> sessionProcessor,
            final ScheduledExecutorService executorService) {
        this.timeStateFactory = checkNotNull(timeStateFactory);
        this.sessionProcessor = checkNotNull(sessionProcessor);
        this.executorService = checkNotNull(executorService);

        this.executorService.scheduleAtFixedRate(new GameTimeRuleActivator(), 1, 1, TimeUnit.SECONDS);
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

    private class GameTimeRuleActivator implements Runnable {

        @Override
        public void run() {
            // Step 1. Polling first entry
            Iterator<GameTimeState> timeStateIterator = timeStateQueue.iterator();
            GameTimeState timeState;
            Collection<Callable<State>> timeoutNotifications = new ArrayList<Callable<State>>();
            // Step 2. Generating appropriate failure events
            while (timeStateIterator.hasNext() && (timeState = timeStateIterator.next()).breached()) {
                final GameTimeBreach gameTimeBreach = timeState.peek();
                if (gameTimeBreach.breached()) {
                    switch (gameTimeBreach.getTimeRule().getPunishment()) {
                    case loose:
                        if (gameTimeBreach.getTimeRule() instanceof MoveTimeRule) {
                            timeoutNotifications.add(new TimeoutNotificationTask(timeState.getSession(), new MoveTimeoutSurrenderEvent(gameTimeBreach
                                    .getPlayerId())));
                        } else {
                            timeoutNotifications.add(new TimeoutNotificationTask(timeState.getSession(), new TotalTimeoutSurrenderEvent(gameTimeBreach
                                    .getPlayerId())));
                        }
                    }
                }
            }
            // Step 3. Invoking events
            try {
                executorService.invokeAll(timeoutNotifications);
            } catch (InterruptedException e) {
                throw GogomayaException.create(GogomayaError.TimeoutProcessingFailure);
            }
        }
    }

    public class TimeoutNotificationTask implements Callable<State>, SessionAware {

        final private long session;
        final private ClientEvent timeoutEvent;

        public TimeoutNotificationTask(final long session, final ClientEvent event) {
            this.session = session;
            this.timeoutEvent = event;
        }

        @Override
        public long getSession() {
            return session;
        }

        @Override
        public State call() throws Exception {
            return sessionProcessor.process(session, timeoutEvent);
        }

    }

}
