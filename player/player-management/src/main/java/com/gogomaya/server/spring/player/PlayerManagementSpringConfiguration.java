package com.gogomaya.server.spring.player;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import com.gogomaya.server.player.registration.PlayerRegistrationService;
import com.gogomaya.server.player.wallet.WalletRegistrationService;
import com.gogomaya.server.repository.player.PlayerCredentialRepository;
import com.gogomaya.server.repository.player.PlayerIdentityRepository;
import com.gogomaya.server.repository.player.PlayerProfileRepository;
import com.gogomaya.server.spring.common.CommonSpringConfiguration;
import com.gogomaya.server.spring.common.SpringConfiguration;

@Configuration
@Import(value = { CommonSpringConfiguration.class, PlayerManagementSpringConfiguration.Test.class, PlayerCommonSpringConfiguration.class })
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
    @Qualifier("walletRegistrationService")
    public WalletRegistrationService walletRegistrationService;

    @Bean
    @Singleton
    public PlayerRegistrationService playerRegistrationService() {
        return new PlayerRegistrationService(playerProfileRepository, playerIdentityRepository, playerCredentialRepository, walletRegistrationService);
    }

    @Configuration
    @Profile("test")
    public static class Test {

        @Bean
        @Singleton
        public WalletRegistrationService walletRegistrationService() {
            return new WalletRegistrationService() {
                @Override
                public void register(long playerId) {
                }
            };
        }

    }
}
