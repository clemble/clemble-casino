package com.clemble.casino.server.spring.web.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.configuration.ResourceLocationService;
import com.clemble.casino.error.GogomayaValidationService;
import com.clemble.casino.server.configuration.ServerLocation;
import com.clemble.casino.server.configuration.ServerRegistryServerService;
import com.clemble.casino.server.configuration.SimpleServerRegistryServerService;
import com.clemble.casino.server.player.account.PlayerAccountServerService;
import com.clemble.casino.server.player.notification.PlayerNotificationRegistry;
import com.clemble.casino.server.player.presence.PlayerPresenceServerService;
import com.clemble.casino.server.player.registration.PlayerProfileRegistrationServerService;
import com.clemble.casino.server.repository.player.PlayerCredentialRepository;
import com.clemble.casino.server.repository.player.PlayerIdentityRepository;
import com.clemble.casino.server.repository.player.PlayerSessionRepository;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.payment.PaymentCommonSpringConfiguration;
import com.clemble.casino.server.spring.player.PlayerCommonSpringConfiguration;
import com.clemble.casino.server.spring.web.WebCommonSpringConfiguration;
import com.clemble.casino.server.web.management.PlayerRegistrationController;
import com.clemble.casino.server.web.management.PlayerSessionController;
import com.clemble.casino.server.web.management.ServerRegistryController;

@Configuration
@Import(value = { WebCommonSpringConfiguration.class, PlayerCommonSpringConfiguration.class, PaymentCommonSpringConfiguration.class })
abstract public class AbstractManagementWebSpringConfiguration implements SpringConfiguration {

    @Autowired
    public PlayerNotificationRegistry playerNotificationRegistry;

    @Autowired
    @Qualifier("paymentEndpointRegistry")
    public ServerLocation paymentEndpointRegistry;

    @Bean
    @Autowired
    public PlayerRegistrationController playerRegistrationController(
            @Qualifier("playerProfileRegistrationService") PlayerProfileRegistrationServerService playerProfileRegistrationService,
            PlayerCredentialRepository playerCredentialRepository,
            PlayerIdentityRepository playerIdentityRepository,
            GogomayaValidationService gogomayaValidationService,
            @Qualifier("playerAccountService") PlayerAccountServerService playerAccountService) {
        return new PlayerRegistrationController(playerProfileRegistrationService, playerCredentialRepository, playerIdentityRepository,
                gogomayaValidationService, playerAccountService);
    }

    @Bean
    @Autowired
    public PlayerSessionController playerSessionController(
            ResourceLocationService resourceLocationService,
            PlayerSessionRepository playerSessionRepository,
            PlayerPresenceServerService playerStateManager) {
        return new PlayerSessionController(resourceLocationService, playerSessionRepository, playerStateManager);
    }

    @Bean
    @Autowired
    public ServerRegistryServerService realServerRegistryService() {
        return new SimpleServerRegistryServerService(playerNotificationRegistry, paymentEndpointRegistry);
    }

    @Bean
    @Autowired
    public ServerRegistryController serverRegistryService(ServerRegistryServerService serverRegistryServerService) {
        return new ServerRegistryController(realServerRegistryService());
    }

}
