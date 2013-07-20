package com.gogomaya.server.spring.web.payment;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gogomaya.server.payment.PaymentTransactionService;
import com.gogomaya.server.player.account.PlayerAccountService;
import com.gogomaya.server.repository.payment.PaymentTransactionRepository;
import com.gogomaya.server.repository.player.PlayerAccountRepository;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.player.account.PaymentManagementSpringConfiguration;
import com.gogomaya.server.spring.web.CommonWebSpringConfiguration;
import com.gogomaya.server.web.payment.PaymentTransactionController;
import com.gogomaya.server.web.player.account.PlayerAccountController;

@Configuration
@Import({ PaymentManagementSpringConfiguration.class, CommonWebSpringConfiguration.class })
public class PaymentWebSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("playerWalletService")
    public PlayerAccountService playerWalletService;

    @Autowired
    @Qualifier("playerAccountRepository")
    public PlayerAccountRepository playerAccountRepository;

    @Autowired
    @Qualifier("paymentTransactionRepository")
    public PaymentTransactionRepository paymentTransactionRepository;

    @Autowired
    @Qualifier("paymentTransactionService")
    public PaymentTransactionService paymentTransactionService;

    @Bean
    @Singleton
    public PaymentTransactionController paymentTransactionController() {
        return new PaymentTransactionController(paymentTransactionRepository, paymentTransactionService);
    }

    @Bean
    @Singleton
    public PlayerAccountController playerAccountController() {
        return new PlayerAccountController(playerAccountRepository, paymentTransactionRepository);
    }

}
