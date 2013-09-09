package com.gogomaya.server.web.management;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gogomaya.server.configuration.ServerRegistryServerService;
import com.gogomaya.server.player.notification.PaymentEndpointRegistry;
import com.gogomaya.server.player.notification.PlayerNotificationRegistry;
import com.gogomaya.web.management.ManagementWebMapping;
import com.gogomaya.web.mapping.WebMapping;

@Controller
public class ServerRegistryController {

    final private ServerRegistryServerService serverRegistryServerService;

    public ServerRegistryController(ServerRegistryServerService serverRegistryServerService) {
        this.serverRegistryServerService = checkNotNull(serverRegistryServerService);
    }

    @RequestMapping(value = ManagementWebMapping.MANAGEMENT_CONFIGURATION_NOTIFICATION, method = RequestMethod.GET, produces = WebMapping.PRODUCES)
    public PlayerNotificationRegistry getPlayerNotificationRegistry() {
        return serverRegistryServerService.getPlayerNotificationRegistry();
    }

    @RequestMapping(value = ManagementWebMapping.MANAGEMENT_CONFIGURATION_PAYMENT, method = RequestMethod.GET, produces = WebMapping.PRODUCES)
    public PaymentEndpointRegistry getPaymentEndpointRegistry() {
        return serverRegistryServerService.getPaymentEndpointRegistry();
    }

}
