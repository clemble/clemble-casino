package com.clemble.casino.server.spring.web.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.server.payment.PaymentTransactionServerService;
import com.clemble.casino.server.player.account.PlayerAccountServerService;
import com.clemble.casino.server.repository.payment.PaymentTransactionRepository;
import com.clemble.casino.server.repository.player.PlayerAccountRepository;
import com.clemble.casino.server.spring.payment.PaymentManagementSpringConfiguration;
import com.clemble.casino.server.web.payment.PaymentTransactionController;
import com.clemble.casino.server.web.player.account.PlayerAccountController;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.web.WebCommonSpringConfiguration;

@Configuration
@Import({
    PaymentManagementSpringConfiguration.class,
    WebCommonSpringConfiguration.class })
public class PaymentWebSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("realPlayerAccountService")
    public PlayerAccountServerService playerAccountService;

    @Autowired
    @Qualifier("paymentTransactionRepository")
    public PaymentTransactionRepository paymentTransactionRepository;

    @Autowired
    @Qualifier("realPaymentTransactionService")
    public PaymentTransactionServerService paymentTransactionService;

    @Autowired
    @Qualifier("playerAccountRepository")
    public PlayerAccountRepository playerAccountRepository;

    @Bean
    public PaymentTransactionController paymentTransactionController() {
        return new PaymentTransactionController(paymentTransactionRepository, paymentTransactionService);
    }

    @Bean
    public PlayerAccountController playerAccountController() {
        return new PlayerAccountController(playerAccountService, playerAccountRepository);
    }

}
