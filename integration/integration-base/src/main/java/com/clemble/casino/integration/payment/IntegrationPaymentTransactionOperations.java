package com.clemble.casino.integration.payment;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.payment.PaymentTransaction;
import static com.clemble.casino.web.payment.PaymentWebMapping.*;

public class IntegrationPaymentTransactionOperations extends AbstractPaymentTransactionOperations {

    final private RestTemplate restTemplate;
    final private String baseUrl;

    public IntegrationPaymentTransactionOperations(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = checkNotNull(restTemplate);
        this.baseUrl = baseUrl;
    }

    @Override
    public PaymentTransaction get(ClembleCasinoOperations player, String source, String transactionId) {
        // Step 1. Performing empty signed GET request
        return restTemplate.exchange(toPaymentUrl(PAYMENT_TRANSACTIONS_TRANSACTION).replace("{host}", baseUrl), HttpMethod.GET, null, PaymentTransaction.class, baseUrl, source, transactionId).getBody();
    }

}
