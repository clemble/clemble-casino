package com.gogomaya.server.spring.web.payment;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gogomaya.server.payment.PaymentTransactionService;
import com.gogomaya.server.player.wallet.PlayerWalletService;
import com.gogomaya.server.repository.payment.PaymentTransactionRepository;
import com.gogomaya.server.repository.player.PlayerWalletRepository;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.player.wallet.PaymentManagementSpringConfiguration;
import com.gogomaya.server.spring.web.CommonWebSpringConfiguration;
import com.gogomaya.server.web.player.wallet.PlayerWalletController;
import com.gogomaya.server.web.player.wallet.PaymentTransactionController;

@Configuration
@Import({ PaymentManagementSpringConfiguration.class, CommonWebSpringConfiguration.class })
public class PaymentWebSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("playerWalletService")
    public PlayerWalletService playerWalletService;

    @Autowired
    @Qualifier("playerWalletRepository")
    public PlayerWalletRepository playerWalletRepository;

    @Autowired
    @Qualifier("paymentTransactionRepository")
    public PaymentTransactionRepository paymentTransactionRepository;

    @Autowired
    @Qualifier("paymentTransactionService")
    public PaymentTransactionService paymentTransactionService;

    @Bean
    @Singleton
    public PaymentTransactionController walletTransactionController() {
        return new PaymentTransactionController(paymentTransactionRepository, paymentTransactionService);
    }

    @Bean
    @Singleton
    public PlayerWalletController walletController() {
        return new PlayerWalletController(playerWalletRepository, paymentTransactionRepository);
    }

}
