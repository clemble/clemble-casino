package com.clemble.casino.client;

import java.io.Closeable;

import org.springframework.social.ApiBinding;

import com.clemble.casino.client.game.GameActionOperations;
import com.clemble.casino.client.game.GameConstructionOperations;
import com.clemble.casino.client.payment.PaymentOperations;
import com.clemble.casino.client.player.PlayerPresenceOperations;
import com.clemble.casino.client.player.PlayerProfileOperations;
import com.clemble.casino.client.player.PlayerSessionOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;

public interface ClembleCasinoOperation extends ApiBinding, Closeable {

    public PlayerProfileOperations profileOperations();

    public PlayerPresenceOperations presenceOperations();

    public PlayerSessionOperations sessionOperations();

    public PaymentOperations paymentOperations();

    public <T extends GameState> GameConstructionOperations<T> gameConstructionOperations(Game game);

    public <State extends GameState> GameActionOperations<State> gameActionOperations(GameSessionKey session);

    @Override
    public void close();
}
