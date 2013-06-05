package com.gogomaya.server.spring.player;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.gogomaya.server.player.PlayerProfileRepository;
import com.gogomaya.server.player.registration.PlayerRegistrationService;
import com.gogomaya.server.player.security.PlayerCredentialRepository;
import com.gogomaya.server.player.security.PlayerIdentityRepository;
import com.gogomaya.server.player.wallet.WalletRegistrationService;
import com.gogomaya.server.spring.common.CommonSpringConfiguration;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.gogomaya.server.player", entityManagerFactoryRef = "entityManagerFactory")
@Import(value = { CommonSpringConfiguration.class, PlayerManagementSpringConfiguration.Test.class })
public class PlayerManagementSpringConfiguration {

    @Inject
    public PlayerIdentityRepository playerIdentityRepository;

    @Inject
    public PlayerCredentialRepository playerCredentialRepository;

    @Inject
    public PlayerProfileRepository playerProfileRepository;

    @Inject
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
