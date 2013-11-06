package com.clemble.casino.server.spring.web.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.configuration.ResourceLocationService;
import com.clemble.casino.configuration.ServerRegistryConfiguration;
import com.clemble.casino.error.ClembleCasinoValidationService;
import com.clemble.casino.server.player.PlayerIdGenerator;
import com.clemble.casino.server.player.UUIDPlayerIdGenerator;
import com.clemble.casino.server.player.account.PlayerAccountServerService;
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

@Configuration
@Import(value = { OAuth2ServerSpringConfiguration.class, WebCommonSpringConfiguration.class, PlayerCommonSpringConfiguration.class, PlayerJPASpringConfiguration.class, PaymentCommonSpringConfiguration.class })
abstract public class AbstractManagementWebSpringConfiguration implements SpringConfiguration {

    @Autowired
    public ServerRegistryConfiguration paymentEndpointRegistry;

    @Bean
    public PlayerIdGenerator playerIdentifierGenerator() {
        return new UUIDPlayerIdGenerator();
    }

    @Bean
    @Autowired
    public PlayerRegistrationController playerRegistrationController(
            PlayerIdGenerator playerIdentifierGenerator,
            @Qualifier("playerProfileRegistrationService") PlayerProfileRegistrationServerService playerProfileRegistrationService,
            PlayerCredentialRepository playerCredentialRepository,
            PlayerIdentityRepository playerIdentityRepository,
            ClembleCasinoValidationService gogomayaValidationService,
            @Qualifier("playerAccountService") PlayerAccountServerService playerAccountService) {
        return new PlayerRegistrationController(playerIdentifierGenerator, playerProfileRegistrationService, playerCredentialRepository, playerIdentityRepository,
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

}
