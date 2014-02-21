package com.clemble.casino.server.spring.payment;

import com.clemble.casino.server.repository.payment.JedisPlayerAccountTemplate;
import com.clemble.casino.server.spring.common.RedisSpringConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

import com.clemble.casino.payment.money.Currency;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.server.payment.BasicServerPaymentTransactionService;
import com.clemble.casino.server.payment.ServerPaymentTransactionService;
import com.clemble.casino.server.payment.bonus.BonusService;
import com.clemble.casino.server.payment.bonus.DailyBonusEventListener;
import com.clemble.casino.server.payment.bonus.PlayerConnectionDiscoveryBonusEventListener;
import com.clemble.casino.server.payment.bonus.PlayerRegisterationBonusEventListener;
import com.clemble.casino.server.payment.bonus.policy.BonusPolicy;
import com.clemble.casino.server.payment.bonus.policy.NoBonusPolicy;
import com.clemble.casino.server.player.account.BasicServerPlayerAccountService;
import com.clemble.casino.server.player.account.ServerPlayerAccountService;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.SystemNotificationServiceListener;
import com.clemble.casino.server.repository.payment.PaymentTransactionRepository;
import com.clemble.casino.server.repository.payment.PlayerAccountTemplate;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.PlayerPresenceSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import redis.clients.jedis.JedisPool;

@Configuration
@Import({ CommonSpringConfiguration.class, PaymentJPASpringConfiguration.class, PlayerPresenceSpringConfiguration.class, RedisSpringConfiguration.class})
public class PaymentManagementSpringConfiguration implements SpringConfiguration {

    @Bean
    public PlayerAccountTemplate playerAccountTemplate(JedisPool jedisPool) {
        return new JedisPlayerAccountTemplate(jedisPool);
    }

    @Bean
    public ServerPlayerAccountService realPlayerAccountService(PlayerAccountTemplate playerAccountRepository) {
        return new BasicServerPlayerAccountService(playerAccountRepository);
    }

    @Bean
    public ServerPaymentTransactionService realPaymentTransactionService(PaymentTransactionRepository paymentTransactionRepository,
            PlayerNotificationService playerNotificationService, PlayerAccountTemplate accountTemplate) {
        return new BasicServerPaymentTransactionService(paymentTransactionRepository, accountTemplate, playerNotificationService);
    }

    @Bean
    public BonusService bonusService(@Qualifier("playerNotificationService") PlayerNotificationService notificationService, PlayerAccountTemplate accountTemplate,
            PaymentTransactionRepository transactionRepository, @Qualifier("realPaymentTransactionService") ServerPaymentTransactionService transactionService,
            BonusPolicy bonusPolicy) {
        return new BonusService(bonusPolicy, notificationService, accountTemplate, transactionRepository, transactionService);
    }

    @Bean
    public DailyBonusEventListener dailyBonusService(BonusService bonusService, SystemNotificationServiceListener notificationServiceListener) {
        Money bonus = new Money(Currency.FakeMoney, 50);
        DailyBonusEventListener dailyBonusService = new DailyBonusEventListener(bonus, bonusService);
        notificationServiceListener.subscribe(dailyBonusService);
        return dailyBonusService;
    }

    @Bean
    public PlayerConnectionDiscoveryBonusEventListener dicoveryBonusService(BonusService bonusService, SystemNotificationServiceListener notificationServiceListener) {
        Money bonus = new Money(Currency.FakeMoney, 100);
        PlayerConnectionDiscoveryBonusEventListener discoveryBonusService = new PlayerConnectionDiscoveryBonusEventListener(bonus, bonusService);
        notificationServiceListener.subscribe(discoveryBonusService);
        return discoveryBonusService;
    }

    @Bean
    public PlayerRegisterationBonusEventListener registerationBonusService(BonusService bonusService, SystemNotificationServiceListener notificationServiceListener) {
        Money bonus = new Money(Currency.FakeMoney, 200);
        PlayerRegisterationBonusEventListener registrationBonusService = new PlayerRegisterationBonusEventListener(bonus, bonusService);
        notificationServiceListener.subscribe(registrationBonusService);
        return registrationBonusService;
    }

    @Bean
    public BonusPolicy bonusPolicy() {
        return new NoBonusPolicy();
    }

}
