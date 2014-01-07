package com.clemble.casino.server.spring.payment;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.payment.money.Currency;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.server.event.SystemPlayerEnteredEvent;
import com.clemble.casino.server.payment.BasicServerPaymentTransactionService;
import com.clemble.casino.server.payment.ServerPaymentTransactionService;
import com.clemble.casino.server.payment.bonus.DailyBonusService;
import com.clemble.casino.server.payment.bonus.policy.BonusPolicy;
import com.clemble.casino.server.payment.bonus.policy.NoBonusPolicy;
import com.clemble.casino.server.player.account.BasicServerPlayerAccountService;
import com.clemble.casino.server.player.account.ServerPlayerAccountService;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
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
    public ServerPlayerAccountService realPlayerAccountService(PlayerAccountRepository playerAccountRepository,
            @Qualifier("realPaymentTransactionService") ServerPaymentTransactionService realPaymentTransactionService) {
        return new BasicServerPlayerAccountService(playerAccountRepository, realPaymentTransactionService);
    }

    @Bean
    public ServerPaymentTransactionService realPaymentTransactionService(
            PaymentTransactionRepository paymentTransactionRepository,
            PlayerNotificationService playerNotificationService,
            PlayerAccountRepository playerAccountRepository) {
        return new BasicServerPaymentTransactionService(paymentTransactionRepository, playerAccountRepository, playerNotificationService);
    }

    @Bean
    public DailyBonusService dailyBonusService(
            PlayerNotificationService playerNotificationService,
            PlayerAccountRepository accountRepository,
            PaymentTransactionRepository transactionRepository,
            @Qualifier("realPaymentTransactionService") ServerPaymentTransactionService transactionServerService,
            BonusPolicy bonusPolicy,
            SystemNotificationServiceListener notificationServiceListener) {
        Money bonus = new Money(Currency.FakeMoney, 100);
        DailyBonusService dailyBonusService = new DailyBonusService(playerNotificationService, accountRepository, transactionRepository, transactionServerService, bonusPolicy, bonus);
        notificationServiceListener.subscribe(dailyBonusService);
        return dailyBonusService;
    }

    @Bean
    public BonusPolicy bonusPolicy() {
        return new NoBonusPolicy();
    }

}
