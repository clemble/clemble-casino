package com.gogomaya.server.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.gogomaya.server.event.Event;
import com.gogomaya.server.game.SessionAware;
import com.gogomaya.server.game.construct.GameConstruction;
import com.gogomaya.server.game.event.server.GameStartedEvent;
import com.gogomaya.server.integration.game.GameSessionListener;
import com.gogomaya.server.integration.player.listener.PlayerListener;
import com.gogomaya.server.integration.player.listener.PlayerListenerControl;
import com.gogomaya.server.integration.player.listener.PlayerListenerOperations;
import com.gogomaya.server.player.PlayerAware;
import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.player.security.PlayerCredential;
import com.gogomaya.server.player.security.PlayerIdentity;
import com.gogomaya.server.player.security.PlayerSession;
import com.gogomaya.server.player.wallet.PlayerWallet;

public class Player implements PlayerAware, Closeable {

    /**
     * Generated
     */
    private static final long serialVersionUID = -4160641502466429770L;

    final private long playerId;
    final private PlayerSession session;
    final private PlayerIdentity identity;
    final private PlayerListenerControl listenerControl;

    final private Object listenerSync = new Object();
    final private Map<Long, GameSessionListener> sessionListeners = new HashMap<Long, GameSessionListener>();
    final private Map<Long, GameSessionListener> pendingConstructionListeners = new HashMap<Long, GameSessionListener>();
    final private ArrayList<Event> events = new ArrayList<Event>();
    final private PlayerOperations playerOperations;

    private PlayerProfile profile;

    private PlayerCredential credential;

    public Player(final PlayerIdentity playerIdentity, final PlayerOperations playerOperations, final PlayerListenerOperations listenerOperations) {
        this.playerOperations = checkNotNull(playerOperations);
        this.identity = checkNotNull(playerIdentity);
        this.playerId = identity.getPlayerId();
        this.session = checkNotNull(playerOperations.startSession(playerIdentity));

        this.listenerControl = listenerOperations.listen(session, new PlayerListener() {
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
        });
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    public PlayerProfile getProfile() {
        return profile;
    }

    public Player setProfile(PlayerProfile profile) {
        this.profile = profile;
        this.profile.setPlayerId(playerId);
        return this;
    }

    public PlayerWallet getWallet() {
        return playerOperations.wallet(this, playerId);
    }

    public PlayerIdentity getIdentity() {
        return identity;
    }

    public PlayerCredential getCredential() {
        return credential;
    }

    public Player setCredential(PlayerCredential credential) {
        this.credential = credential;
        return this;
    }

    public PlayerSession getSession() {
        return session;
    }

    public PlayerListenerControl getListenerControl() {
        return listenerControl;
    }

    public void listen(long session, GameSessionListener sessionListener) {
        synchronized (listenerSync) {
            // Step 1. Sanity check
            if (sessionListeners.containsKey(session))
                throw new IllegalArgumentException("Multiple listeners are not supported");
            // Step 2. Adding SessionListener
            sessionListeners.put(session, sessionListener);
            // Step 3. Notifying of all the events that already happened, related to this session
            for (Event event : events) {
                if (event instanceof SessionAware && (((SessionAware) event).getSession()) == session) {
                    sessionListener.update(event);
                }
            }
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
        listenerControl.stopListener();
    }

}
