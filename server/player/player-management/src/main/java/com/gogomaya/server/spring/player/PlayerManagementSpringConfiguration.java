package com.gogomaya.server.spring.player;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gogomaya.server.player.account.PlayerAccountProcessingService;
import com.gogomaya.server.player.registration.PlayerRegistrationProcessingService;
import com.gogomaya.server.repository.player.PlayerCredentialRepository;
import com.gogomaya.server.repository.player.PlayerIdentityRepository;
import com.gogomaya.server.repository.player.PlayerProfileRepository;
import com.gogomaya.server.spring.common.CommonSpringConfiguration;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.payment.PaymentCommonSpringConfiguration;

@Configuration
@Import(value = { CommonSpringConfiguration.class, PaymentCommonSpringConfiguration.class, PlayerCommonSpringConfiguration.class })
public class PlayerManagementSpringConfiguration implements SpringConfiguration {

    @Inject
    @Named("playerIdentityRepository")
    public PlayerIdentityRepository playerIdentityRepository;

    @Inject
    @Named("playerCredentialRepository")
    public PlayerCredentialRepository playerCredentialRepository;

    @Inject
    @Named("playerProfileRepository")
    public PlayerProfileRepository playerProfileRepository;

    @Inject
    @Named("playerAccountService")
    public PlayerAccountProcessingService playerAccountService;

    @Bean
    @Singleton
    public PlayerRegistrationProcessingService playerRegistrationService() {
        return new PlayerRegistrationProcessingService(playerProfileRepository, playerIdentityRepository, playerCredentialRepository, playerAccountService);
    }

}
