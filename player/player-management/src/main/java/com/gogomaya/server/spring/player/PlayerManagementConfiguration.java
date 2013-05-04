package com.gogomaya.server.spring.player;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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
import com.gogomaya.server.spring.common.CommonModuleSpringConfiguration;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.gogomaya.server.player", entityManagerFactoryRef = "entityManagerFactory")
@ComponentScan(basePackages = "com.gogomaya.server.player")
@Import(value = { CommonModuleSpringConfiguration.class, PlayerManagementConfiguration.TestPlayerManagementConfiguration.class })
public class PlayerManagementConfiguration {

    @Inject
    PlayerIdentityRepository playerIdentityRepository;

    @Inject
    PlayerCredentialRepository playerCredentialRepository;

    @Inject
    PlayerProfileRepository playerProfileRepository;

    @Inject
    WalletRegistrationService walletRegistrationService;

    @Bean
    @Singleton
    public PlayerRegistrationService playerRegistrationService() {
        return new PlayerRegistrationService(playerProfileRepository, playerIdentityRepository, playerCredentialRepository, walletRegistrationService);
    }

    @Configuration
    @Profile("test")
    public static class TestPlayerManagementConfiguration {

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
