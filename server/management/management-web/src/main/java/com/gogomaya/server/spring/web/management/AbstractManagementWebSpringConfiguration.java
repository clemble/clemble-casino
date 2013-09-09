package com.gogomaya.server.spring.web.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gogomaya.configuration.ResourceLocationService;
import com.gogomaya.error.GogomayaValidationService;
import com.gogomaya.server.configuration.ServerRegistryServerService;
import com.gogomaya.server.configuration.SimpleServerRegistryServerService;
import com.gogomaya.server.player.account.PlayerAccountServerService;
import com.gogomaya.server.player.notification.PaymentEndpointRegistry;
import com.gogomaya.server.player.notification.PlayerNotificationRegistry;
import com.gogomaya.server.player.registration.PlayerProfileRegistrationServerService;
import com.gogomaya.server.player.state.PlayerStateManager;
import com.gogomaya.server.repository.player.PlayerCredentialRepository;
import com.gogomaya.server.repository.player.PlayerIdentityRepository;
import com.gogomaya.server.repository.player.PlayerSessionRepository;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.payment.PaymentCommonSpringConfiguration;
import com.gogomaya.server.spring.player.PlayerCommonSpringConfiguration;
import com.gogomaya.server.spring.web.WebCommonSpringConfiguration;
import com.gogomaya.server.web.management.PlayerRegistrationController;
import com.gogomaya.server.web.management.PlayerSessionController;
import com.gogomaya.server.web.management.ServerRegistryController;

@Configuration
@Import(value = { WebCommonSpringConfiguration.class, PlayerCommonSpringConfiguration.class, PaymentCommonSpringConfiguration.class })
abstract public class AbstractManagementWebSpringConfiguration implements SpringConfiguration {

    @Autowired
    public PlayerNotificationRegistry playerNotificationRegistry;

    @Autowired
    public PaymentEndpointRegistry paymentEndpointRegistry;

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
            PlayerStateManager playerStateManager) {
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
