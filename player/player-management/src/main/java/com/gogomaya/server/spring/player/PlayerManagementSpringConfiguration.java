package com.gogomaya.server.spring.player;

import javax.inject.Inject;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.gogomaya.server.player.wallet.PlayerMoneyTransactionManager;
import com.gogomaya.server.player.wallet.PlayerTransactionManagerImpl;
import com.gogomaya.server.player.wallet.PlayerWalletRepository;
import com.gogomaya.server.spring.common.CommonModuleSpringConfiguration;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.gogomaya.server.player", entityManagerFactoryRef = "entityManagerFactory")
@ComponentScan(basePackages = "com.gogomaya.server.player")
@Import(value = { CommonModuleSpringConfiguration.class })
public class PlayerManagementSpringConfiguration {

    @Inject
    PlayerWalletRepository playerWalletRepository;

    public PlayerMoneyTransactionManager playerWalletManager() {
        return new PlayerTransactionManagerImpl(playerWalletRepository);
    }

}
