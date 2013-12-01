package com.clemble.casino.integration.player.account;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.List;

import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.payment.service.PaymentService;
import com.clemble.casino.payment.service.PaymentTransactionService;
import com.clemble.casino.payment.service.PlayerAccountService;

public class CombinedPaymentService implements PaymentService {

    final private PaymentTransactionService paymentTransactionService;
    final private PlayerAccountService playerAccountService;

    public CombinedPaymentService(PaymentTransactionService transactionService, PlayerAccountService accountService) {
        this.playerAccountService = checkNotNull(accountService);
        this.paymentTransactionService = checkNotNull(transactionService);
    }

    @Override
    public PlayerAccount get(String playerId) {
        return playerAccountService.get(playerId);
    }

    @Override
    public PaymentTransaction getPaymentTransaction(String source, String transactionId) {
        return paymentTransactionService.getPaymentTransaction(source, transactionId);
    }

    @Override
    public List<PaymentTransaction> getPaymentTransactions(String player) {
        return paymentTransactionService.getPaymentTransactions(player);
    }

}
