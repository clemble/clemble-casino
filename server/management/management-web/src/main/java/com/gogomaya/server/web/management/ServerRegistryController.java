package com.gogomaya.server.web.management;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gogomaya.server.configuration.ServerLocation;
import com.gogomaya.server.configuration.ServerRegistryServerService;
import com.gogomaya.server.player.notification.PlayerNotificationRegistry;
import com.gogomaya.web.management.ManagementWebMapping;
import com.gogomaya.web.mapping.WebMapping;

@Controller
public class ServerRegistryController implements ServerRegistryServerService {

    final private ServerRegistryServerService serverRegistryServerService;

    public ServerRegistryController(ServerRegistryServerService serverRegistryServerService) {
        this.serverRegistryServerService = checkNotNull(serverRegistryServerService);
    }

    @Override
    @RequestMapping(value = ManagementWebMapping.MANAGEMENT_CONFIGURATION_NOTIFICATION, method = RequestMethod.GET, produces = WebMapping.PRODUCES)
    public @ResponseBody PlayerNotificationRegistry getPlayerNotificationRegistry() {
        return serverRegistryServerService.getPlayerNotificationRegistry();
    }

    @Override
    @RequestMapping(value = ManagementWebMapping.MANAGEMENT_CONFIGURATION_PAYMENT, method = RequestMethod.GET, produces = WebMapping.PRODUCES)
    public @ResponseBody ServerLocation getPayment() {
        return serverRegistryServerService.getPayment();
    }

}
