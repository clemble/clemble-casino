package com.clemble.casino.integration.payment;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.configuration.ResourceLocations;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.payment.service.PaymentService;
import com.clemble.casino.web.payment.PaymentWebMapping;

public class IntegrationPaymentService implements PaymentService {

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
        return restTemplate.exchange(getPaymentEndpoint(player) + PaymentWebMapping.PAYMENT_ACCOUNTS_PLAYER, HttpMethod.GET, null, PlayerAccount.class,
                playerId).getBody();
    }

    @Override
    public PaymentTransaction getPaymentTransaction(String source, String transactionId) {
        // Step 1. Requesting account associated with the playerId
        return restTemplate.exchange(getPaymentEndpoint(player) + PaymentWebMapping.PAYMENT_TRANSACTIONS_TRANSACTION, HttpMethod.GET, null,
                PaymentTransaction.class, source, transactionId).getBody();

    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public List<PaymentTransaction> listPlayerTransaction(String playerId) {
        // Step 1. Requesting account associated with the playerId
        return restTemplate.exchange(getPaymentEndpoint(player) + PaymentWebMapping.PAYMENT_ACCOUNTS_PLAYER_TRANSACTIONS, HttpMethod.GET, null, List.class , playerId).getBody();
    }

    @Override
    public PaymentTransaction process(PaymentTransaction paymentTransaction) {
        HttpEntity<PaymentTransaction> request = new HttpEntity<PaymentTransaction>(paymentTransaction);
        // Step 2. Requesting account associated with the playerId
        return restTemplate.exchange(getPaymentEndpoint(player) + PaymentWebMapping.PAYMENT_TRANSACTIONS_TRANSACTION, HttpMethod.GET, request,
                PaymentTransaction.class).getBody();
    }

}
