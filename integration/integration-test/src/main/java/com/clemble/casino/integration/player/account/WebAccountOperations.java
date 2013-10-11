package com.clemble.casino.integration.player.account;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.clemble.casino.integration.player.Player;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.server.web.payment.PaymentTransactionController;
import com.clemble.casino.server.web.player.account.PlayerAccountController;

public class WebAccountOperations extends AbstractAccountOperations {

    final private PaymentTransactionController paymentTransactionController;
    final private PlayerAccountController playerAccountController;

    public WebAccountOperations(PaymentTransactionController paymentTransactionController, PlayerAccountController playerAccountController) {
        this.paymentTransactionController = checkNotNull(paymentTransactionController);
        this.playerAccountController = checkNotNull(playerAccountController);
    }

    @Override
    public PlayerAccount getAccount(Player player, String playerId) {
        return playerAccountController.get(playerId);
    }

    @Override
    public List<PaymentTransaction> getTransactions(Player player, String playerId) {
        return paymentTransactionController.listPlayerTransaction(playerId);
    }

    @Override
    public PaymentTransaction getTransaction(Player player, String playerId, String source, String transactionId) {
        return paymentTransactionController.getPaymentTransaction(playerId, source, transactionId);
    }

}
