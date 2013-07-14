package com.gogomaya.server.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.construct.GameConstruction;
import com.gogomaya.server.integration.game.GameSessionListener;
import com.gogomaya.server.integration.game.construction.GameConstructionOperations;
import com.gogomaya.server.integration.game.construction.PlayerGameConstructionOperations;
import com.gogomaya.server.integration.player.listener.PlayerListenerManager;
import com.gogomaya.server.integration.player.listener.PlayerListenerOperations;
import com.gogomaya.server.integration.player.profile.PlayerProfileOperations;
import com.gogomaya.server.player.PlayerAware;
import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.player.security.PlayerCredential;
import com.gogomaya.server.player.security.PlayerIdentity;
import com.gogomaya.server.player.security.PlayerSession;
import com.gogomaya.server.player.wallet.PlayerWallet;
import com.google.common.collect.ImmutableMap;

public class Player implements PlayerAware {

    /**
     * Generated
     */
    private static final long serialVersionUID = -4160641502466429770L;

    final private long playerId;
    final private PlayerSession session;
    final private PlayerIdentity identity;
    final private PlayerListenerManager playerListenersManager;
    final private PlayerCredential credential;

    final private Map<String, PlayerGameConstructionOperations<?>> gameConstructors;

    final private PlayerOperations playerOperations;
    final private PlayerProfileOperations profileOperations;

    public Player(final PlayerIdentity playerIdentity,
            final PlayerCredential credential,
            final PlayerOperations playerOperations,
            final PlayerProfileOperations playerProfileOperations,
            final PlayerListenerOperations listenerOperations,
            final GameConstructionOperations<?>... playerConstructionOperations) {
        this.playerOperations = checkNotNull(playerOperations);
        this.profileOperations = checkNotNull(playerProfileOperations);
        this.identity = checkNotNull(playerIdentity);
        this.playerId = identity.getPlayerId();
        this.session = checkNotNull(playerOperations.startSession(playerIdentity));
        this.credential = checkNotNull(credential);
        this.playerListenersManager = new PlayerListenerManager(this, listenerOperations);

        Map<String, PlayerGameConstructionOperations<?>> map = new HashMap<>();
        for (GameConstructionOperations<?> constructionOperation : playerConstructionOperations) {
            map.put(constructionOperation.getName(), new PlayerGameConstructionOperations<>(constructionOperation, this));
        }
        this.gameConstructors = ImmutableMap.<String, PlayerGameConstructionOperations<?>> copyOf(map);
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    public PlayerProfile getProfile() {
        return profileOperations.get(this, playerId);
    }

    public Player setProfile(PlayerProfile newProfile) {
        profileOperations.put(this, playerId, newProfile);
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

    public PlayerSession getSession() {
        return session;
    }

    public <State extends GameState> PlayerGameConstructionOperations<State> getGameConstructor(String name) {
        return (PlayerGameConstructionOperations<State>) gameConstructors.get(name);
    }

    public Map<String, PlayerGameConstructionOperations<?>> getGameConstructors() {
        return gameConstructors;
    }

    public void listen(long session, GameSessionListener sessionListener) {
        playerListenersManager.listen(session, sessionListener);
    }

    public void listen(GameConstruction construction, GameSessionListener sessionListener) {
        playerListenersManager.listen(construction, sessionListener);
    }

    public void close() {
        playerListenersManager.close();
    }

}
