package com.gogomaya.server.spring.player;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gogomaya.server.payment.PaymentTransactionService;
import com.gogomaya.server.player.registration.PlayerRegistrationService;
import com.gogomaya.server.repository.player.PlayerCredentialRepository;
import com.gogomaya.server.repository.player.PlayerIdentityRepository;
import com.gogomaya.server.repository.player.PlayerProfileRepository;
import com.gogomaya.server.spring.common.CommonSpringConfiguration;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.payment.PaymentCommonSpringConfiguration;

@Configuration
@Import(value = { CommonSpringConfiguration.class, PaymentCommonSpringConfiguration.class, PlayerCommonSpringConfiguration.class })
public class PlayerManagementSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("playerIdentityRepository")
    public PlayerIdentityRepository playerIdentityRepository;

    @Autowired
    @Qualifier("playerCredentialRepository")
    public PlayerCredentialRepository playerCredentialRepository;

    @Autowired
    @Qualifier("playerProfileRepository")
    public PlayerProfileRepository playerProfileRepository;

    @Autowired
    @Qualifier("paymentTransactionService")
    public PaymentTransactionService paymentTransactionService;

    @Bean
    @Singleton
    public PlayerRegistrationService playerRegistrationService() {
        return new PlayerRegistrationService(playerProfileRepository, playerIdentityRepository, playerCredentialRepository, paymentTransactionService);
    }

}
