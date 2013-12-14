package com.clemble.casino.server.spring.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.payment.money.Currency;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.server.payment.PaymentTransactionServerService;
import com.clemble.casino.server.payment.PaymentTransactionServerServiceImpl;
import com.clemble.casino.server.payment.bonus.DailyBonusService;
import com.clemble.casino.server.payment.bonus.policy.BonusPolicy;
import com.clemble.casino.server.payment.bonus.policy.NoBonusPolicy;
import com.clemble.casino.server.player.account.PlayerAccountServerService;
import com.clemble.casino.server.player.account.PlayerAccountServerServiceImpl;
import com.clemble.casino.server.player.presence.SystemNotificationServiceListener;
import com.clemble.casino.server.repository.payment.PaymentTransactionRepository;
import com.clemble.casino.server.repository.payment.PlayerAccountRepository;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.PlayerPresenceSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;

@Configuration
@Import({ CommonSpringConfiguration.class, PaymentJPASpringConfiguration.class, PlayerPresenceSpringConfiguration.class })
public class PaymentManagementSpringConfiguration implements SpringConfiguration {

    @Bean
    @Autowired
    public PlayerAccountServerService realPlayerAccountService(PlayerAccountRepository playerAccountRepository,
            @Qualifier("realPaymentTransactionService") PaymentTransactionServerService realPaymentTransactionService) {
        return new PlayerAccountServerServiceImpl(playerAccountRepository, realPaymentTransactionService);
    }

    @Bean
    @Autowired
    public PaymentTransactionServerService realPaymentTransactionService(PaymentTransactionRepository paymentTransactionRepository,
            PlayerAccountRepository playerAccountRepository) {
        return new PaymentTransactionServerServiceImpl(paymentTransactionRepository, playerAccountRepository);
    }

    @Bean
    @Autowired
    public DailyBonusService dailyBonusService(PlayerAccountRepository accountRepository,
            PaymentTransactionRepository transactionRepository,
            @Qualifier("realPaymentTransactionService") PaymentTransactionServerService transactionServerService,
            BonusPolicy bonusPolicy,
            SystemNotificationServiceListener notificationServiceListener) {
        Money bonus = new Money(Currency.FakeMoney, 100);
        DailyBonusService dailyBonusService = new DailyBonusService(accountRepository, transactionRepository, transactionServerService, bonusPolicy, bonus);
        notificationServiceListener.subscribe("entered", dailyBonusService);
        return dailyBonusService;
    }

    @Bean
    public BonusPolicy bonusPolicy() {
        return new NoBonusPolicy();
    }

}
