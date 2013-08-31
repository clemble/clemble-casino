package com.gogomaya.server.spring.payment;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gogomaya.server.payment.PaymentTransactionProcessingService;
import com.gogomaya.server.payment.PaymentTransactionProcessingServiceImpl;
import com.gogomaya.server.player.account.PlayerAccountProcessingService;
import com.gogomaya.server.player.account.PlayerAccountServiceImpl;
import com.gogomaya.server.repository.payment.PaymentTransactionRepository;
import com.gogomaya.server.repository.player.PlayerAccountRepository;
import com.gogomaya.server.spring.common.CommonSpringConfiguration;
import com.gogomaya.server.spring.common.SpringConfiguration;

@Configuration
@Import(CommonSpringConfiguration.class)
public class PaymentManagementSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("playerAccountRepository")
    public PlayerAccountRepository playerAccountRepository;

    @Autowired
    @Qualifier("paymentTransactionRepository")
    public PaymentTransactionRepository paymentTransactionRepository;

    @Bean
    @Singleton
    public PlayerAccountProcessingService realPlayerAccountService() {
        return new PlayerAccountServiceImpl(playerAccountRepository, realPaymentTransactionService());
    }

    @Bean
    @Singleton
    public PaymentTransactionProcessingService realPaymentTransactionService() {
        return new PaymentTransactionProcessingServiceImpl(paymentTransactionRepository, playerAccountRepository);
    }

}
