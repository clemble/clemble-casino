package com.clemble.casino.client;

import org.springframework.social.ApiBinding;

import com.clemble.casino.client.game.GameActionOperations;
import com.clemble.casino.client.game.GameConstructionOperations;
import com.clemble.casino.client.payment.PaymentTransactionOperations;
import com.clemble.casino.client.player.PlayerPresenceOperations;
import com.clemble.casino.client.player.PlayerProfileOperations;
import com.clemble.casino.client.player.PlayerSessionOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;

public interface ClembleCasino extends ApiBinding {

    public PlayerProfileOperations getPlayerProfileOperations();

    public PlayerPresenceOperations getPlayerPresenceOperations();

    public PlayerSessionOperations getPlayerSessionOperations();

    public PaymentTransactionOperations getPaymentTransactionOperations();

    public <T extends GameState> GameConstructionOperations<T> getGameConstructionOperations(Game game);

    public <State extends GameState> GameActionOperations<State> getGameActionOperations(GameSessionKey session);

}
