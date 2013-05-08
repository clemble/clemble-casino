package com.gogomaya.server.spring.web.payment;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.gogomaya.server.player.wallet.PlayerWalletRepository;
import com.gogomaya.server.player.wallet.WalletTransactionManager;
import com.gogomaya.server.spring.player.wallet.PlayerWalletManagementSpringConfiguration;
import com.gogomaya.server.web.player.wallet.WalletController;
import com.gogomaya.server.web.player.wallet.WalletTransactionController;

@Configuration
@Import(PlayerWalletManagementSpringConfiguration.class)
public class WebPaymentConfiguration extends WebMvcConfigurationSupport {

    @Inject
    public WalletTransactionManager playerTransactionManager;

    @Inject
    public PlayerWalletRepository walletRepository;

    @Bean
    @Singleton
    public WalletTransactionController walletTransactionController() {
        return new WalletTransactionController(playerTransactionManager);
    }

    @Bean
    @Singleton
    public WalletController walletController() {
        return new WalletController(walletRepository);
    }

}
