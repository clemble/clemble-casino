package com.clemble.casino.integration.player;

import java.util.Map;

import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.client.game.GameConstructionOperations;
import com.clemble.casino.client.payment.PaymentOperations;
import com.clemble.casino.client.player.PlayerProfileOperations;
import com.clemble.casino.client.player.PlayerSessionOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.player.security.PlayerCredential;
import com.clemble.casino.player.security.PlayerSession;
import com.clemble.casino.player.security.PlayerToken;

public interface Player extends PlayerAware {

    public PlayerSessionOperations sessionOperations();

    public PlayerProfileOperations profileOperations();

    public PaymentOperations paymentOperations();

    public PlayerToken getIdentity();

    public PlayerCredential getCredential();

    public PlayerSession getSession();

    public <State extends GameState> GameConstructionOperations<State> gameConstructionOperations(Game game);

    public Map<Game, GameConstructionOperations<?>> gameConstructionOperations();

    public void listen(GameSessionKey session, EventListener sessionListener);

    public void close();

}
