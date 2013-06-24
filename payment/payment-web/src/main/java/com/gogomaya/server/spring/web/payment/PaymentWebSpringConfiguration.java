package com.gogomaya.server.spring.web.payment;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gogomaya.server.player.wallet.PlayerWalletRepository;
import com.gogomaya.server.player.wallet.WalletTransactionManager;
import com.gogomaya.server.spring.player.wallet.PaymentManagementSpringConfiguration;
import com.gogomaya.server.spring.web.CommonWebSpringConfiguration;
import com.gogomaya.server.web.player.wallet.WalletController;
import com.gogomaya.server.web.player.wallet.WalletTransactionController;

@Configuration
@Import({ PaymentManagementSpringConfiguration.class, CommonWebSpringConfiguration.class })
public class PaymentWebSpringConfiguration {

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
