package com.gogomaya.server.integration.player.account;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.payment.PlayerAccount;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.payment.web.mapping.PaymentWebMapping;

public class IntegrationAccountOperations extends AbstractAccountOperations {

    final private RestTemplate restTemplate;
    final private String baseUrl;

    public IntegrationAccountOperations(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = checkNotNull(restTemplate);
        this.baseUrl = checkNotNull(baseUrl);
    }

    @Override
    public PlayerAccount getAccount(Player player, long playerId) {
        // Step 1. Generating request
        HttpEntity<Void> request = player.<Void> sign(null);
        // Step 2. Requesting account associated with the playerId
        return restTemplate.exchange(baseUrl + PaymentWebMapping.ACCOUNT_PREFIX + PaymentWebMapping.PAYMENT_ACCOUNTS_PLAYER, HttpMethod.GET, request, PlayerAccount.class,
                playerId).getBody();
    }

    @Override
    public List<PaymentTransaction> getTransactions(Player player, long playerId) {
        // Step 1. Generating request
        HttpEntity<Void> request = player.<Void> sign(null);
        // Step 2. Requesting account associated with the playerId
        return restTemplate.exchange(baseUrl + PaymentWebMapping.ACCOUNT_PREFIX + PaymentWebMapping.PAYMENT_ACCOUNTS_PLAYER_TRANSACTIONS, HttpMethod.GET, request,
                new ParameterizedTypeReference<List<PaymentTransaction>>() {
                }, playerId).getBody();
    }

    @Override
    public PaymentTransaction getTransaction(Player player, long playerId, String moneySource, long transactionId) {
        // Step 1. Generating request
        HttpEntity<Void> request = player.<Void> sign(null);
        // Step 2. Requesting account associated with the playerId
        return restTemplate.exchange(baseUrl + PaymentWebMapping.ACCOUNT_PREFIX + PaymentWebMapping.PAYMENT_ACCOUNTS_PLAYER_TRANSACTIONS_TRANSACTION, HttpMethod.GET,
                request, PaymentTransaction.class, playerId, moneySource, transactionId).getBody();

    }

}
