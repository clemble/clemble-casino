package com.gogomaya.server.spring.player.wallet;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.gogomaya.server.player.wallet.WalletRegistrationService;
import com.gogomaya.server.player.wallet.WalletRegistrationServiceImpl;
import com.gogomaya.server.player.wallet.WalletTransactionManager;
import com.gogomaya.server.player.wallet.WalletTransactionManagerImpl;
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
    public WalletTransactionManager walletTransactionManager() {
        return new WalletTransactionManagerImpl(walletRepository);
    }

    @Bean
    @Singleton
    public WalletRegistrationService walletRegistrationService() {
        return new WalletRegistrationServiceImpl(walletRepository);
    }

}
