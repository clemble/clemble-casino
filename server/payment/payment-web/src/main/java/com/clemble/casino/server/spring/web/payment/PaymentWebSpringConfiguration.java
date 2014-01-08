package com.clemble.casino.server.spring.web.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.server.payment.ServerPaymentTransactionService;
import com.clemble.casino.server.player.account.ServerPlayerAccountService;
import com.clemble.casino.server.repository.payment.PaymentTransactionRepository;
import com.clemble.casino.server.repository.payment.PlayerAccountTemplate;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.payment.PaymentManagementSpringConfiguration;
import com.clemble.casino.server.spring.web.WebCommonSpringConfiguration;
import com.clemble.casino.server.web.payment.ServerPaymentTransactionController;
import com.clemble.casino.server.web.player.account.PlayerAccountController;

@Configuration
@Import({
    PaymentManagementSpringConfiguration.class,
    WebCommonSpringConfiguration.class })
public class PaymentWebSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("realPlayerAccountService")
    public ServerPlayerAccountService playerAccountService;

    @Autowired
    @Qualifier("paymentTransactionRepository")
    public PaymentTransactionRepository paymentTransactionRepository;

    @Autowired
    @Qualifier("realPaymentTransactionService")
    public ServerPaymentTransactionService paymentTransactionService;

    @Autowired
    public PlayerAccountTemplate accountTemplate;

    @Bean
    public ServerPaymentTransactionController paymentTransactionController() {
        return new ServerPaymentTransactionController(paymentTransactionRepository, paymentTransactionService);
    }

    @Bean
    public PlayerAccountController playerAccountController() {
        return new PlayerAccountController(playerAccountService, accountTemplate);
    }

}
