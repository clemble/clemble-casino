package com.clemble.casino.integration.payment;

import java.util.Collection;
import java.util.List;

import com.clemble.casino.payment.money.Currency;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.configuration.ResourceLocations;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.payment.service.PaymentService;
import com.clemble.casino.server.payment.ServerPaymentTransactionService;
import com.clemble.casino.utils.CollectionUtils;
import static com.clemble.casino.web.payment.PaymentWebMapping.*;

import static com.clemble.casino.web.payment.PaymentWebMapping.PAYMENT_ACCOUNTS;

public class IntegrationPaymentService implements PaymentService, ServerPaymentTransactionService {

    final private RestTemplate restTemplate;
    final private ClembleCasinoOperations player;

    public IntegrationPaymentService(ClembleCasinoOperations player, RestTemplate restTemplate) {
        this.player = player;
        this.restTemplate = restTemplate;
    }

    private String getPaymentEndpoint(ClembleCasinoOperations player) {
        ResourceLocations resourceLocations = player.sessionOperations().create().getResourceLocations();
        return resourceLocations.getServerRegistryConfiguration().getPaymentRegistry().findBase();
    }

    @Override
    public PlayerAccount get(String playerId) {
        // Step 1. Requesting account associated with the playerId
        return restTemplate.exchange(getPaymentEndpoint(player) + ACCOUNTS_PLAYER, HttpMethod.GET, null, PlayerAccount.class, playerId).getBody();
    }

    @Override
    public List<String> canAfford(@RequestParam("player") Collection<String> players, @RequestParam("currency") Currency currency, @RequestParam("amount") Long amount) {
        // Step 1. Generating URL
        String url = getPaymentEndpoint(player) + PAYMENT_ACCOUNTS + "?currency=" + currency + "&amount=" + amount;
        for(String player: players)
            url += "&player=" + player;
        // Step 2. Sending and receiving response
        return CollectionUtils.immutableList(restTemplate.getForObject(url, String[].class));

    }

    @Override
    public PaymentTransaction getTransaction(String source, String transactionId) {
        // Step 1. Requesting account associated with the playerId
        return restTemplate.exchange(getPaymentEndpoint(player) + PAYMENT_TRANSACTIONS_TRANSACTION, HttpMethod.GET, null,
                PaymentTransaction.class, source, transactionId).getBody();
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public List<PaymentTransaction> getPlayerTransactions(String playerId) {
        // Step 1. Requesting account associated with the playerId
        return restTemplate.exchange(getPaymentEndpoint(player) + PAYMENT_ACCOUNTS_PLAYER_TRANSACTIONS, HttpMethod.GET, null, List.class , playerId).getBody();
    }

    @Override
    public PaymentTransaction process(PaymentTransaction paymentTransaction) {
        HttpEntity<PaymentTransaction> request = new HttpEntity<PaymentTransaction>(paymentTransaction);
        // Step 2. Requesting account associated with the playerId
        return restTemplate.exchange(getPaymentEndpoint(player) + PAYMENT_TRANSACTIONS_TRANSACTION, HttpMethod.POST, request,
                PaymentTransaction.class).getBody();
    }

    @Override
    public List<PaymentTransaction> getPlayerTransactionsWithSource(String playerId, String source) {
        return CollectionUtils.immutableList(restTemplate.exchange(getPaymentEndpoint(player) + PAYMENT_ACCOUNTS_PLAYER_TRANSACTION_SOURCE, HttpMethod.GET, null, PaymentTransaction[].class, playerId, source).getBody());
    }

}
