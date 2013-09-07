package com.gogomaya.server.payment;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.web.client.RestTemplate;

import com.gogomaya.payment.PaymentTransaction;
import com.gogomaya.server.configuration.ServerRegistryService;
import com.gogomaya.web.payment.PaymentWebMapping;

public class RestPaymentTransactionServerService implements PaymentTransactionServerService {

    final private RestTemplate restTemplate;
    final private ServerRegistryService serverRegistryService;

    public RestPaymentTransactionServerService(ServerRegistryService serverRegistryService, RestTemplate restTemplate) {
        this.serverRegistryService = checkNotNull(serverRegistryService);
        this.restTemplate = checkNotNull(restTemplate);
    }

    @Override
    public PaymentTransaction process(PaymentTransaction paymentTransaction) {
        return restTemplate.postForEntity(serverRegistryService.getPaymentEndpointRegistry().getPaymentEndpoint() + PaymentWebMapping.PAYMENT_TRANSACTIONS,
                paymentTransaction, PaymentTransaction.class).getBody();
    }

}
