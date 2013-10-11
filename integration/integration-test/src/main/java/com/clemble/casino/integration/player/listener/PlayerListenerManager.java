package com.clemble.casino.integration.player.listener;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.SessionAware;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.integration.game.GameSessionListener;
import com.clemble.casino.integration.player.Player;

public class PlayerListenerManager implements PlayerListener, Closeable {

    final private Object listenerSync = new Object();
    final private Map<GameSessionKey, Collection<GameSessionListener>> sessionListenersMap = new HashMap<>();
    final private ArrayList<Event> events = new ArrayList<Event>();
    final private PlayerListenerControl listenerControl;

    public PlayerListenerManager(Player player, PlayerListenerOperations playerListenerOperations) {
        listenerControl = playerListenerOperations.listen(player.getSession(), this);
    }

    @Override
    public void updated(Event event) {
        synchronized (listenerSync) {
            events.add(event);
            if (event instanceof SessionAware) {
                GameSessionKey session = ((SessionAware) event).getSession();
                Collection<GameSessionListener> sessionListeners = sessionListenersMap.get(session);
                if (sessionListeners != null && sessionListeners.size() > 0)
                    for (GameSessionListener sessionListener : sessionListeners)
                        sessionListener.update(event);
            }
        }
    }

    public void listen(GameSessionKey session, GameSessionListener sessionListener) {
        synchronized (listenerSync) {
            // Step 1. Sanity check
            if (!sessionListenersMap.containsKey(session))
                sessionListenersMap.put(session, new ArrayList<GameSessionListener>());
            // Step 2. Notifying of all the events that already happened, related to this session
            for (Event event : events) {
                if (event instanceof SessionAware && (((SessionAware) event).getSession()).equals(session)) {
                    sessionListener.update(event);
                }
            }
            // Step 3. Adding SessionListener
            sessionListenersMap.get(session).add(sessionListener);
        }
    }

    public void listen(GameConstruction construction, GameSessionListener sessionListener) {
        synchronized (listenerSync) {
            listen(construction.getSession(), sessionListener);
        }
    }

    @Override
    public void close() {
        listenerControl.close();
    }

}
