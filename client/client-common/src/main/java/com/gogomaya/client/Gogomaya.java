package com.gogomaya.client;

import com.gogomaya.client.game.service.GameActionOperations;
import com.gogomaya.client.game.service.GameConstructionOperations;
import com.gogomaya.client.payment.service.PaymentTransactionOperations;
import com.gogomaya.client.player.service.PlayerProfileOperations;
import com.gogomaya.game.Game;
import com.gogomaya.game.GameState;

public interface Gogomaya {

    public PlayerProfileOperations getPlayerProfileOperations();

    public PaymentTransactionOperations getPaymentTransactionOperations();

    public GameConstructionOperations getGameConstructionOperations(Game game);

    public <State extends GameState> GameActionOperations<State> getGameActionOperations(Game game, long sessionId);

}
