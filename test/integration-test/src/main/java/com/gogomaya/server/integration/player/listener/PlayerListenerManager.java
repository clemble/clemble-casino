package com.gogomaya.server.integration.player.listener;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.gogomaya.server.event.Event;
import com.gogomaya.server.game.SessionAware;
import com.gogomaya.server.game.construct.GameConstruction;
import com.gogomaya.server.game.event.server.GameStartedEvent;
import com.gogomaya.server.integration.game.GameSessionListener;
import com.gogomaya.server.integration.player.Player;

public class PlayerListenerManager implements PlayerListener, Closeable {

    final private Object listenerSync = new Object();
    final private Map<Long, GameSessionListener> sessionListeners = new HashMap<Long, GameSessionListener>();
    final private Map<Long, GameSessionListener> pendingConstructionListeners = new HashMap<Long, GameSessionListener>();
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
                long session = ((SessionAware) event).getSession();
                if (event instanceof GameStartedEvent<?>) {
                    GameSessionListener sessionListener = pendingConstructionListeners.remove(((GameStartedEvent<?>) event).getConstruction());
                    if (sessionListener != null)
                        sessionListeners.put(session, sessionListener);
                }
                GameSessionListener sessionListener = sessionListeners.get(session);
                if (sessionListener != null)
                    sessionListener.update(event);
            }
        }
    }

    public void listen(long session, GameSessionListener sessionListener) {
        synchronized (listenerSync) {
            // Step 1. Sanity check
            if (sessionListeners.containsKey(session))
                throw new IllegalArgumentException("Multiple listeners are not supported");
            // Step 2. Notifying of all the events that already happened, related to this session
            for (Event event : events) {
                if (event instanceof SessionAware && (((SessionAware) event).getSession()) == session) {
                    sessionListener.update(event);
                }
            }
            // Step 3. Adding SessionListener
            sessionListeners.put(session, sessionListener);
        }
    }

    public void listen(GameConstruction construction, GameSessionListener sessionListener) {
        synchronized (listenerSync) {
            if (construction.getSession() != 0) {
                listen(construction.getSession(), sessionListener);
            } else {
                pendingConstructionListeners.put(construction.getConstruction(), sessionListener);
            }
        }
    }

    @Override
    public void close() {
        listenerControl.close();
    }

}
