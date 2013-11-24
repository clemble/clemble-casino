package com.clemble.casino.integration.payment;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.clemble.casino.ServerRegistry;
import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.web.payment.PaymentWebMapping;

public class IntegrationPaymentTransactionOperations extends AbstractPaymentTransactionOperations {

    final private RestTemplate restTemplate;
    final private ServerRegistry baseUrl;

    public IntegrationPaymentTransactionOperations(RestTemplate restTemplate, ServerRegistry baseUrl) {
        this.restTemplate = checkNotNull(restTemplate);
        this.baseUrl = baseUrl;
    }

    @Override
    public PaymentTransaction perform(PaymentTransaction transaction) {
        return restTemplate.postForEntity(baseUrl.findBase() + PaymentWebMapping.PAYMENT_TRANSACTIONS, transaction,
                PaymentTransaction.class).getBody();
    }

    @Override
    public PaymentTransaction get(ClembleCasinoOperations player, String source, String transactionId) {
        // Step 1. Performing empty signed GET request
        return restTemplate.exchange(baseUrl.findBase() + PaymentWebMapping.PAYMENT_TRANSACTIONS_TRANSACTION, HttpMethod.GET, null, PaymentTransaction.class, source, transactionId).getBody();
    }

}
