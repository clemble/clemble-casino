package com.gogomaya.server.spring.web.payment;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gogomaya.server.player.wallet.WalletTransactionManager;
import com.gogomaya.server.repository.payment.PlayerWalletRepository;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.player.wallet.PaymentManagementSpringConfiguration;
import com.gogomaya.server.spring.web.CommonWebSpringConfiguration;
import com.gogomaya.server.web.player.wallet.WalletController;
import com.gogomaya.server.web.player.wallet.WalletTransactionController;

@Configuration
@Import({ PaymentManagementSpringConfiguration.class, CommonWebSpringConfiguration.class })
public class PaymentWebSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("walletTransactionManager")
    public WalletTransactionManager walletTransactionManager;

    @Autowired
    @Qualifier("playerWalletRepository")
    public PlayerWalletRepository playerWalletRepository;

    @Bean
    @Singleton
    public WalletTransactionController walletTransactionController() {
        return new WalletTransactionController(walletTransactionManager);
    }

    @Bean
    @Singleton
    public WalletController walletController() {
        return new WalletController(playerWalletRepository);
    }

}
