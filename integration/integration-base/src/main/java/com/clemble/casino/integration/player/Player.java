package com.clemble.casino.integration.player;

import java.util.Map;

import org.springframework.http.HttpEntity;

import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.client.game.GameConstructionOperations;
import com.clemble.casino.client.payment.PaymentOperations;
import com.clemble.casino.client.player.PlayerProfileOperations;
import com.clemble.casino.client.player.PlayerSessionOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.security.PlayerCredential;
import com.clemble.casino.player.security.PlayerSession;
import com.clemble.casino.player.security.PlayerToken;

public interface Player extends PlayerAware {

    public PlayerSessionOperations getSessionOperations();

    public PlayerProfileOperations getProfileOperations();

    public PaymentOperations getWalletOperations();

    public <T extends PlayerProfile> T getProfile();

    public Player setProfile(PlayerProfile newProfile);

    public PlayerToken getIdentity();

    public PlayerCredential getCredential();

    public PlayerSession getSession();

    public <State extends GameState> GameConstructionOperations<State> getGameConstructor(Game game);

    public Map<Game, GameConstructionOperations<?>> getGameConstructors();

    public <T> HttpEntity<T> sign(T value);

    public <T> HttpEntity<T> signGame(GameSessionKey session, T value);

    public void listen(GameSessionKey session, EventListener sessionListener);

    public void close();

}
