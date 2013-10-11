package com.clemble.casino.client;

import com.clemble.casino.client.game.service.GameActionOperations;
import com.clemble.casino.client.game.service.GameConstructionOperations;
import com.clemble.casino.client.payment.service.PaymentTransactionOperations;
import com.clemble.casino.client.player.service.PlayerPresenceOperations;
import com.clemble.casino.client.player.service.PlayerProfileOperations;
import com.clemble.casino.client.player.service.PlayerSessionOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;

public interface Gogomaya {

    public PlayerProfileOperations getPlayerProfileOperations();

    public PlayerPresenceOperations getPlayerPresenceOperations();

    public PlayerSessionOperations getPlayerSessionOperations();

    public PaymentTransactionOperations getPaymentTransactionOperations();

    public GameConstructionOperations getGameConstructionOperations(Game game);

    public <State extends GameState> GameActionOperations<State> getGameActionOperations(GameSessionKey session);

}
