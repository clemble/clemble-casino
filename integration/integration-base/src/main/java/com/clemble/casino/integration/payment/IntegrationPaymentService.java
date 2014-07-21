package com.clemble.casino.integration.payment;

import java.util.Collection;
import java.util.List;

import com.clemble.casino.payment.money.Currency;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.payment.service.PaymentService;

public class IntegrationPaymentService implements PaymentService {

    final private RestTemplate restTemplate;
    final private ClembleCasinoOperations player;

    public IntegrationPaymentService(ClembleCasinoOperations player, RestTemplate restTemplate) {
        this.player = player;
        this.restTemplate = restTemplate;
    }

    @Override
    public PlayerAccount get(String playerId) {
        return player.paymentOperations().getAccount();
    }

    @Override
    public List<String> canAfford(@RequestParam("player") Collection<String> players, @RequestParam("currency") Currency currency, @RequestParam("amount") Long amount) {
        return player.paymentOperations().canAfford(players, currency, amount);
    }

    @Override
    public PaymentTransaction getTransaction(String source, String transactionId) {
        return player.paymentOperations().getPaymentTransaction(source, transactionId);
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public List<PaymentTransaction> getPlayerTransactions(String playerId) {
        return player.paymentOperations().getPaymentTransactions();
    }

    @Override
    public List<PaymentTransaction> getPlayerTransactionsWithSource(String playerId, String source) {
        return player.paymentOperations().getPaymentTransactions(source);
    }

}
