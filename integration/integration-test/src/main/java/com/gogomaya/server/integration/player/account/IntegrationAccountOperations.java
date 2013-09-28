package com.gogomaya.server.integration.player.account;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.configuration.ResourceLocations;
import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.payment.PlayerAccount;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.web.payment.PaymentWebMapping;

public class IntegrationAccountOperations extends AbstractAccountOperations {

    final private RestTemplate restTemplate;

    public IntegrationAccountOperations(RestTemplate restTemplate) {
        this.restTemplate = checkNotNull(restTemplate);
    }

    @Override
    public PlayerAccount getAccount(Player player, String playerId) {
        ResourceLocations resourceLocations = player.getSession().getResourceLocations();
        // Step 1. Generating request
        HttpEntity<Void> request = player.<Void> sign(null);
        // Step 2. Requesting account associated with the playerId
        return restTemplate.exchange(resourceLocations.getPaymentEndpoint() + PaymentWebMapping.PAYMENT_ACCOUNTS_PLAYER, HttpMethod.GET, request, PlayerAccount.class,
                playerId).getBody();
    }

    @Override
    public List<PaymentTransaction> getTransactions(Player player, String playerId) {
        ResourceLocations resourceLocations = player.getSession().getResourceLocations();
        // Step 1. Generating request
        HttpEntity<Void> request = player.<Void> sign(null);
        // Step 2. Requesting account associated with the playerId
        return restTemplate.exchange(resourceLocations.getPaymentEndpoint() + PaymentWebMapping.PAYMENT_ACCOUNTS_PLAYER_TRANSACTIONS, HttpMethod.GET, request,
                new ParameterizedTypeReference<List<PaymentTransaction>>() {
                }, playerId).getBody();
    }

    @Override
    public PaymentTransaction getTransaction(Player player, String playerId, String moneySource, String transactionId) {
        ResourceLocations resourceLocations = player.getSession().getResourceLocations();
        // Step 1. Generating request
        HttpEntity<Void> request = player.<Void> sign(null);
        // Step 2. Requesting account associated with the playerId
        return restTemplate.exchange(resourceLocations.getPaymentEndpoint() + PaymentWebMapping.PAYMENT_TRANSACTIONS_TRANSACTION, HttpMethod.GET,
                request, PaymentTransaction.class, moneySource, transactionId).getBody();

    }

}
