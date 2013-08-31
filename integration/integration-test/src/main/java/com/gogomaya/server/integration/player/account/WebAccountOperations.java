package com.gogomaya.server.integration.player.account;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.payment.PlayerAccount;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.web.payment.PaymentTransactionController;
import com.gogomaya.server.web.player.account.PlayerAccountController;

public class WebAccountOperations extends AbstractAccountOperations {

    final private PaymentTransactionController paymentTransactionController;
    final private PlayerAccountController playerAccountController;

    public WebAccountOperations(PaymentTransactionController paymentTransactionController, PlayerAccountController playerAccountController) {
        this.paymentTransactionController = checkNotNull(paymentTransactionController);
        this.playerAccountController = checkNotNull(playerAccountController);
    }

    @Override
    public PlayerAccount getAccount(Player player, long playerId) {
        return playerAccountController.get(playerId);
    }

    @Override
    public List<PaymentTransaction> getTransactions(Player player, long playerId) {
        return paymentTransactionController.listPlayerTransaction(playerId);
    }

    @Override
    public PaymentTransaction getTransaction(Player player, long playerId, String source, long transactionId) {
        return paymentTransactionController.getPaymentTransaction(playerId, source, transactionId);
    }

}
