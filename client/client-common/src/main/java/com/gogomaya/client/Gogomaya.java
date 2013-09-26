package com.gogomaya.client;

import com.gogomaya.client.game.service.GameActionOperations;
import com.gogomaya.client.game.service.GameConstructionOperations;
import com.gogomaya.client.payment.service.PaymentTransactionOperations;
import com.gogomaya.client.player.service.PlayerPresenceOperations;
import com.gogomaya.client.player.service.PlayerProfileOperations;
import com.gogomaya.client.player.service.PlayerSessionOperations;
import com.gogomaya.game.Game;
import com.gogomaya.game.GameSessionKey;
import com.gogomaya.game.GameState;

public interface Gogomaya {

    public PlayerProfileOperations getPlayerProfileOperations();

    public PlayerPresenceOperations getPlayerPresenceOperations();

    public PlayerSessionOperations getPlayerSessionOperations();

    public PaymentTransactionOperations getPaymentTransactionOperations();

    public GameConstructionOperations getGameConstructionOperations(Game game);

    public <State extends GameState> GameActionOperations<State> getGameActionOperations(GameSessionKey session);

}
