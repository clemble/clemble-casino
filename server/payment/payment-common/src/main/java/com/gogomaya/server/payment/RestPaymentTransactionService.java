package com.gogomaya.server.payment;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.web.client.RestTemplate;

import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.web.mapping.PaymentWebMapping;

public class RestPaymentTransactionService implements PaymentTransactionProcessingService {

    final private String baseUrl;
    final private RestTemplate restTemplate;
    
    public RestPaymentTransactionService(String baseUrl, RestTemplate restTemplate) {
        this.baseUrl = checkNotNull(baseUrl);
        this.restTemplate = checkNotNull(restTemplate);
    }

    @Override
    public PaymentTransaction process(PaymentTransaction paymentTransaction) {
        return restTemplate.postForEntity(baseUrl + PaymentWebMapping.ACCOUNT_PREFIX + PaymentWebMapping.PAYMENT_TRANSACTIONS, paymentTransaction,
                PaymentTransaction.class).getBody();
    }

}
