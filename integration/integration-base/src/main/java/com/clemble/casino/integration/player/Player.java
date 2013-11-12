package com.clemble.casino.integration.player;

import java.util.Map;

import org.springframework.http.HttpEntity;

import com.clemble.casino.client.player.PlayerSessionOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.integration.game.GameSessionListener;
import com.clemble.casino.integration.game.construction.PlayerGameConstructionOperations;
import com.clemble.casino.integration.player.account.PlayerAccountOperations;
import com.clemble.casino.integration.player.profile.PlayerProfileOperations;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.security.PlayerCredential;
import com.clemble.casino.player.security.PlayerSession;
import com.clemble.casino.player.security.PlayerToken;

public interface Player extends PlayerAware {

    public PlayerSessionOperations getSessionOperations();

    public PlayerProfileOperations getProfileOperations();

    public PlayerAccountOperations getWalletOperations();

    public <T extends PlayerProfile> T getProfile();

    public Player setProfile(PlayerProfile newProfile);

    public PlayerToken getIdentity();

    public PlayerCredential getCredential();

    public PlayerSession getSession();

    public <State extends GameState> PlayerGameConstructionOperations<State> getGameConstructor(Game game);

    public Map<Game, PlayerGameConstructionOperations<?>> getGameConstructors();

    public <T> HttpEntity<T> sign(T value);

    public <T> HttpEntity<T> signGame(GameSessionKey session, T value);

    public void listen(GameSessionKey session, GameSessionListener sessionListener);

    public void listen(GameConstruction construction, GameSessionListener sessionListener);

    public void close();

}
