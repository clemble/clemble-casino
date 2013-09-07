package com.gogomaya.server.configuration;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.web.client.RestTemplate;

import com.gogomaya.server.player.notification.PaymentEndpointRegistry;
import com.gogomaya.server.player.notification.PlayerNotificationRegistry;
import com.gogomaya.web.management.ManagementWebMapping;

public class RestServerRegistryService implements ServerRegistryService {

    final private String baseUrl;
    final private RestTemplate restTemplate;

    public RestServerRegistryService(String baseUrl, RestTemplate restTemplate) {
        this.baseUrl = checkNotNull(baseUrl);
        this.restTemplate = checkNotNull(restTemplate);
    }

    @Override
    public PlayerNotificationRegistry getPlayerNotificationRegistry() {
        return restTemplate.getForEntity(baseUrl + ManagementWebMapping.MANAGEMENT_CONFIGURATION_NOTIFICATION, PlayerNotificationRegistry.class).getBody();
    }

    @Override
    public PaymentEndpointRegistry getPaymentEndpointRegistry() {
        return restTemplate.getForEntity(baseUrl + ManagementWebMapping.MANAGEMENT_CONFIGURATION_PAYMENT, PaymentEndpointRegistry.class).getBody();
    }

}
