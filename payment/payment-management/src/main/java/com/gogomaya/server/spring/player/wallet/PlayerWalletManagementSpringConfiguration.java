package com.gogomaya.server.spring.player.wallet;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.gogomaya.server.player.wallet.PlayerTransactionManager;
import com.gogomaya.server.player.wallet.PlayerTransactionManagerImpl;
import com.gogomaya.server.player.wallet.PlayerWalletRepository;
import com.gogomaya.server.spring.common.CommonModuleSpringConfiguration;

@Configuration
@EnableJpaRepositories(basePackages = "com.gogomaya.server.player.wallet", entityManagerFactoryRef = "entityManagerFactory")
@Import(CommonModuleSpringConfiguration.class)
public class PlayerWalletManagementSpringConfiguration {

    @Inject
    public PlayerWalletRepository walletRepository;

    @Bean
    @Singleton
    public PlayerTransactionManager playerTransactionManager() {
        return new PlayerTransactionManagerImpl(walletRepository);
    }

}
