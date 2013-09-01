package com.gogomaya.server.payment;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.web.client.RestTemplate;

import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.web.payment.PaymentWebMapping;

public class RestPaymentTransactionServerService implements PaymentTransactionServerService {

    final private String baseUrl;
    final private RestTemplate restTemplate;
    
    public RestPaymentTransactionServerService(String baseUrl, RestTemplate restTemplate) {
        this.baseUrl = checkNotNull(baseUrl);
        this.restTemplate = checkNotNull(restTemplate);
    }

    @Override
    public PaymentTransaction process(PaymentTransaction paymentTransaction) {
        return restTemplate.postForEntity(baseUrl + PaymentWebMapping.PAYMENT_PREFIX + PaymentWebMapping.PAYMENT_TRANSACTIONS, paymentTransaction,
                PaymentTransaction.class).getBody();
    }

}
