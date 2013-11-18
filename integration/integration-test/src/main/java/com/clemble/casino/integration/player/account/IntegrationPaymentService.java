package com.clemble.casino.integration.player.account;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.clemble.casino.configuration.ResourceLocations;
import com.clemble.casino.integration.player.Player;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.payment.service.PaymentService;
import com.clemble.casino.web.payment.PaymentWebMapping;

public class IntegrationPaymentService implements PaymentService {

    final private RestTemplate restTemplate;
    final private Player player;

    public IntegrationPaymentService(Player player, RestTemplate restTemplate) {
        this.player = player;
        this.restTemplate = restTemplate;
    }

    private String getPaymentEndpoint(Player player) {
        ResourceLocations resourceLocations = player.getSession().getResourceLocations();
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
    public List<PaymentTransaction> listPlayerTransaction(String playerId) {
        // Step 1. Requesting account associated with the playerId
        return restTemplate.exchange(getPaymentEndpoint(player) + PaymentWebMapping.PAYMENT_ACCOUNTS_PLAYER_TRANSACTIONS, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<PaymentTransaction>>() {
                }, playerId).getBody();
    }

    @Override
    public PaymentTransaction process(PaymentTransaction paymentTransaction) {
        HttpEntity<PaymentTransaction> request = new HttpEntity<PaymentTransaction>(paymentTransaction);
        // Step 2. Requesting account associated with the playerId
        return restTemplate.exchange(getPaymentEndpoint(player) + PaymentWebMapping.PAYMENT_TRANSACTIONS_TRANSACTION, HttpMethod.GET, request,
                PaymentTransaction.class).getBody();
    }

}
