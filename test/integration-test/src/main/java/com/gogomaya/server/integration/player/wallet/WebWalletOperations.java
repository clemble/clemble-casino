package com.gogomaya.server.integration.player.wallet;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.payment.PaymentTransaction;
import com.gogomaya.server.player.wallet.PlayerWallet;
import com.gogomaya.server.web.player.wallet.PlayerWalletController;

public class WebWalletOperations extends AbstractWalletOperations {

    final private PlayerWalletController playerWalletController;

    public WebWalletOperations(PlayerWalletController playerWalletController) {
        this.playerWalletController = checkNotNull(playerWalletController);
    }

    @Override
    public PlayerWallet getWallet(Player player, long playerId) {
        return playerWalletController.get(player.getPlayerId(), playerId);
    }

    @Override
    public List<PaymentTransaction> getTransactions(Player player, long playerId) {
        return playerWalletController.listPlayerTransaction(player.getPlayerId(), playerId);
    }

    @Override
    public PaymentTransaction getTransaction(Player player, long playerId, String source, long transactionId) {
        return playerWalletController.getPlayerTransaction(player.getPlayerId(), playerId, source, transactionId);
    }

}
