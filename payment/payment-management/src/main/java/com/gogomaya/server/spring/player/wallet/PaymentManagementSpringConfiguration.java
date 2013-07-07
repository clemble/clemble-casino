package com.gogomaya.server.spring.player.wallet;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gogomaya.server.player.wallet.WalletRegistrationService;
import com.gogomaya.server.player.wallet.WalletRegistrationServiceImpl;
import com.gogomaya.server.player.wallet.WalletTransactionManager;
import com.gogomaya.server.player.wallet.WalletTransactionManagerImpl;
import com.gogomaya.server.repository.payment.PlayerWalletRepository;
import com.gogomaya.server.repository.payment.WalletTransactionRepository;
import com.gogomaya.server.spring.common.CommonSpringConfiguration;
import com.gogomaya.server.spring.common.SpringConfiguration;

@Configuration
@Import(CommonSpringConfiguration.class)
public class PaymentManagementSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("playerWalletRepository")
    public PlayerWalletRepository walletRepository;

    @Autowired
    @Qualifier("walletTransactionRepository")
    public WalletTransactionRepository walletTransactionRepository;

    @Bean
    @Singleton
    public WalletTransactionManager walletTransactionManager() {
        return new WalletTransactionManagerImpl(walletRepository, walletTransactionRepository);
    }

    @Bean
    @Singleton
    public WalletRegistrationService walletRegistrationService() {
        return new WalletRegistrationServiceImpl(walletRepository);
    }

}
