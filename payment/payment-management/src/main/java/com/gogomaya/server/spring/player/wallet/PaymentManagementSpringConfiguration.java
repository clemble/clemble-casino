package com.gogomaya.server.spring.player.wallet;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gogomaya.server.payment.PaymentTransactionService;
import com.gogomaya.server.payment.PaymentTransactionServiceImpl;
import com.gogomaya.server.player.wallet.PlayerWalletService;
import com.gogomaya.server.player.wallet.PlayerWalletServiceImpl;
import com.gogomaya.server.repository.payment.PaymentTransactionRepository;
import com.gogomaya.server.repository.player.PlayerWalletRepository;
import com.gogomaya.server.spring.common.CommonSpringConfiguration;
import com.gogomaya.server.spring.common.SpringConfiguration;

@Configuration
@Import(CommonSpringConfiguration.class)
public class PaymentManagementSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("playerWalletRepository")
    public PlayerWalletRepository playerWalletRepository;

    @Autowired
    @Qualifier("paymentTransactionRepository")
    public PaymentTransactionRepository paymentTransactionRepository;

    @Bean
    @Singleton
    public PlayerWalletService walletTransactionManager() {
        return new PlayerWalletServiceImpl(playerWalletRepository);
    }
    
    @Bean
    @Singleton
    public PaymentTransactionService paymentTransactionService() {
        return new PaymentTransactionServiceImpl(paymentTransactionRepository, playerWalletRepository);
    }

}
