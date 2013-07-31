package com.gogomaya.server.integration.payment;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.payment.PaymentTransaction;
import com.gogomaya.server.payment.web.mapping.PaymentWebMapping;

public class IntegrationPaymentTransactionOperations extends AbstractPaymentTransactionOperations {

    final private RestTemplate restTemplate;
    final private String baseUrl;

    public IntegrationPaymentTransactionOperations(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = checkNotNull(restTemplate);
        this.baseUrl = baseUrl;
    }

    @Override
    public PaymentTransaction perform(PaymentTransaction transaction) {
        return restTemplate.postForEntity(baseUrl + PaymentWebMapping.ACCOUNT_PREFIX + PaymentWebMapping.PAYMENT_TRANSACTIONS, transaction,
                PaymentTransaction.class).getBody();
    }

    @Override
    public PaymentTransaction get(Player player, String source, long transactionId) {
        // Step 1. Generating empty signed request
        HttpEntity<Void> signedRequest = player.sign(null);
        // Step 2. Performing empty signed GET request
        return restTemplate.exchange(baseUrl + PaymentWebMapping.ACCOUNT_PREFIX + PaymentWebMapping.PAYMENT_TRANSACTIONS_TRANSACTION, HttpMethod.GET, signedRequest,
                PaymentTransaction.class, source, transactionId).getBody();
    }

}
