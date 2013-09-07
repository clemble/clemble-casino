package com.gogomaya.server.spring.web.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gogomaya.configuration.ResourceLocationService;
import com.gogomaya.error.GogomayaValidationService;
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
    @Qualifier("playerCredentialRepository")
    public PlayerCredentialRepository playerCredentialRepository;;

    @Autowired
    @Qualifier("playerIdentityRepository")
    public PlayerIdentityRepository playerIdentityRepository;

    @Autowired
    @Qualifier("playerProfileRegistrationService")
    public PlayerProfileRegistrationServerService playerProfileRegistrationService;

    @Autowired
    @Qualifier("gogomayaValidationService")
    public GogomayaValidationService validationService;

    @Autowired
    @Qualifier("playerAccountService")
    public PlayerAccountServerService playerAccountService;

    @Autowired
    @Qualifier("resourceLocationService")
    public ResourceLocationService resourceLocationService;

    @Autowired
    @Qualifier("playerSessionRepository")
    public PlayerSessionRepository playerSessionRepository;

    @Autowired
    @Qualifier("playerStateManager")
    public PlayerStateManager playerStateManager;

    @Autowired
    @Qualifier("playerNotificationRegistry")
    public PlayerNotificationRegistry playerNotificationRegistry;
    
    @Autowired
    @Qualifier("paymentEndpointRegistry")
    public PaymentEndpointRegistry paymentEndpointRegistry;

    @Bean
    public PlayerRegistrationController playerRegistrationController() {
        return new PlayerRegistrationController(playerProfileRegistrationService, playerCredentialRepository, playerIdentityRepository, validationService,
                playerAccountService);
    }

    @Bean
    public PlayerSessionController playerSessionController() {
        return new PlayerSessionController(resourceLocationService, playerSessionRepository, playerStateManager);
    }

    @Bean
    public ServerRegistryController serverRegistryService() {
        return new ServerRegistryController(playerNotificationRegistry, paymentEndpointRegistry);
    }

}
