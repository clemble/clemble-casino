package com.gogomaya.server.configuration;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.web.client.RestTemplate;

import com.gogomaya.server.player.notification.PlayerNotificationRegistry;
import com.gogomaya.web.management.ManagementWebMapping;

public class RestServerRegistryServerService implements ServerRegistryServerService {

    final private String baseUrl;
    final private RestTemplate restTemplate;

    public RestServerRegistryServerService(String baseUrl, RestTemplate restTemplate) {
        this.baseUrl = checkNotNull(baseUrl);
        this.restTemplate = checkNotNull(restTemplate);
    }

    @Override
    public PlayerNotificationRegistry getPlayerNotificationRegistry() {
        return restTemplate.getForEntity(baseUrl + ManagementWebMapping.MANAGEMENT_CONFIGURATION_NOTIFICATION, PlayerNotificationRegistry.class).getBody();
    }

    @Override
    public ServerLocation getPayment() {
        return restTemplate.getForEntity(baseUrl + ManagementWebMapping.MANAGEMENT_CONFIGURATION_PAYMENT, ServerLocation.class).getBody();
    }

}
