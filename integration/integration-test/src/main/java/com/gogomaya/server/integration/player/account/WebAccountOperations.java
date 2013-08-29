package com.gogomaya.server.integration.player.account;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.payment.PlayerAccount;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.web.player.account.PlayerAccountController;

public class WebAccountOperations extends AbstractAccountOperations {

    final private PlayerAccountController playerAccountController;

    public WebAccountOperations(PlayerAccountController playerAccountController) {
        this.playerAccountController = checkNotNull(playerAccountController);
    }

    @Override
    public PlayerAccount getAccount(Player player, long playerId) {
        return playerAccountController.get(player.getPlayerId(), playerId);
    }

    @Override
    public List<PaymentTransaction> getTransactions(Player player, long playerId) {
        return playerAccountController.listPlayerTransaction(player.getPlayerId(), playerId);
    }

    @Override
    public PaymentTransaction getTransaction(Player player, long playerId, String source, long transactionId) {
        return playerAccountController.getPlayerTransaction(player.getPlayerId(), playerId, source, transactionId);
    }

}
