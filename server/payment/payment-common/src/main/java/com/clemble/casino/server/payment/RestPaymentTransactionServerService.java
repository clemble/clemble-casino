package com.clemble.casino.server.payment;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.web.client.RestTemplate;

import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.web.payment.PaymentWebMapping;
import com.clemble.casino.server.configuration.ServerRegistryServerService;

public class RestPaymentTransactionServerService implements PaymentTransactionServerService {

    final private RestTemplate restTemplate;
    final private ServerRegistryServerService serverRegistryService;

    public RestPaymentTransactionServerService(ServerRegistryServerService serverRegistryService, RestTemplate restTemplate) {
        this.serverRegistryService = checkNotNull(serverRegistryService);
        this.restTemplate = checkNotNull(restTemplate);
    }

    @Override
    public PaymentTransaction process(PaymentTransaction paymentTransaction) {
        return restTemplate.postForEntity(serverRegistryService.getPayment().getLocation() + PaymentWebMapping.PAYMENT_TRANSACTIONS,
                paymentTransaction, PaymentTransaction.class).getBody();
    }

}
