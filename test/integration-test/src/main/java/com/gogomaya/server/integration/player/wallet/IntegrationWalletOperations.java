package com.gogomaya.server.integration.player.wallet;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.payment.PaymentTransaction;
import com.gogomaya.server.player.wallet.PlayerWallet;
import com.gogomaya.server.web.mapping.PaymentWebMapping;

public class IntegrationWalletOperations extends AbstractWalletOperations {

    final private RestTemplate restTemplate;
    final private String baseUrl;

    public IntegrationWalletOperations(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = checkNotNull(restTemplate);
        this.baseUrl = checkNotNull(baseUrl);
    }

    @Override
    public PlayerWallet getWallet(Player player, long playerId) {
        // Step 1. Generating request
        HttpEntity<Void> request = player.<Void> sign(null);
        // Step 2. Requesting wallet associated with the playerId
        return restTemplate.exchange(baseUrl + PaymentWebMapping.WALLET_PREFIX + PaymentWebMapping.WALLET_PLAYER, HttpMethod.GET, request, PlayerWallet.class,
                playerId).getBody();
    }

    @Override
    public List<PaymentTransaction> getTransactions(Player player, long playerId) {
        // Step 1. Generating request
        HttpEntity<Void> request = player.<Void> sign(null);
        // Step 2. Requesting wallet associated with the playerId
        return restTemplate.exchange(baseUrl + PaymentWebMapping.WALLET_PREFIX + PaymentWebMapping.WALLET_PLAYER_TRANSACTIONS, HttpMethod.GET, request,
                new ParameterizedTypeReference<List<PaymentTransaction>>() {
                }, playerId).getBody();
    }

    @Override
    public PaymentTransaction getTransaction(Player player, long playerId, String moneySource, long transactionId) {
        // Step 1. Generating request
        HttpEntity<Void> request = player.<Void> sign(null);
        // Step 2. Requesting wallet associated with the playerId
        return restTemplate.exchange(baseUrl + PaymentWebMapping.WALLET_PREFIX + PaymentWebMapping.WALLET_PLAYER_TRANSACTIONS_TRANSACTION, HttpMethod.GET,
                request, PaymentTransaction.class, playerId, moneySource, transactionId).getBody();

    }

}
