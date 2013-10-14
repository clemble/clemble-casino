package com.clemble.casino.server.spring.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.server.payment.PaymentTransactionServerService;
import com.clemble.casino.server.payment.PaymentTransactionServerServiceImpl;
import com.clemble.casino.server.player.account.PlayerAccountServerService;
import com.clemble.casino.server.player.account.PlayerAccountServerServiceImpl;
import com.clemble.casino.server.repository.payment.PaymentTransactionRepository;
import com.clemble.casino.server.repository.player.PlayerAccountRepository;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;

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
    public PlayerAccountServerService realPlayerAccountService() {
        return new PlayerAccountServerServiceImpl(playerAccountRepository, realPaymentTransactionService());
    }

    @Bean
    public PaymentTransactionServerService realPaymentTransactionService() {
        return new PaymentTransactionServerServiceImpl(paymentTransactionRepository, playerAccountRepository);
    }

}
