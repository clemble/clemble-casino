package com.clemble.casino.server.bonus.spring;

import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.server.bonus.BonusService;
import com.clemble.casino.server.bonus.listener.DailyBonusEventListener;
import com.clemble.casino.server.bonus.listener.DiscoveryBonusEventListener;
import com.clemble.casino.server.bonus.listener.RegistrationBonusEventListener;
import com.clemble.casino.server.bonus.listener.SocialAddedBonusEventListener;
import com.clemble.casino.server.bonus.policy.BonusPolicy;
import com.clemble.casino.server.bonus.policy.NoBonusPolicy;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;

/**
 * Created by mavarazy on 7/5/14.
 */
@Configuration
@Import({ CommonSpringConfiguration.class})
public class PaymentBonusSpringConfiguration implements SpringConfiguration {

    @Bean
    public BonusService bonusService(
        SystemNotificationService systemNotificationService,
        BonusPolicy bonusPolicy) {
        return new BonusService(bonusPolicy, systemNotificationService);
    }

    @Bean
    @DependsOn("bonusService")
    public DailyBonusEventListener dailyBonusService(
            BonusService bonusService,
            SystemNotificationServiceListener notificationServiceListener,
            @Value("${clemble.bonus.daily.currency}") Currency dailyCurrency,
            @Value("${clemble.bonus.daily.amount}") int dailyBonus) {
        Money bonus = new Money(dailyCurrency, dailyBonus);
        DailyBonusEventListener dailyBonusService = new DailyBonusEventListener(bonus, bonusService);
        notificationServiceListener.subscribe(dailyBonusService);
        return dailyBonusService;
    }

    @Bean
    @DependsOn("bonusService")
    public DiscoveryBonusEventListener dicoveryBonusService(
            BonusService bonusService,
            SystemNotificationServiceListener notificationServiceListener,
            @Value("${clemble.bonus.discovery.currency}") Currency discoveryCurrency,
            @Value("${clemble.bonus.discovery.amount}") int discoveryBonus) {
        Money bonus = new Money(discoveryCurrency, discoveryBonus);
        DiscoveryBonusEventListener discoveryBonusService = new DiscoveryBonusEventListener(bonus, bonusService);
        notificationServiceListener.subscribe(discoveryBonusService);
        return discoveryBonusService;
    }

    @Bean
    @DependsOn("bonusService")
    public RegistrationBonusEventListener registrationBonusService(
            BonusService bonusService,
            SystemNotificationServiceListener notificationServiceListener,
            @Value("${clemble.bonus.registration.currency}") Currency registrationCurrency,
            @Value("${clemble.bonus.registration.amount}") int registrationBonus) {
        Money bonus = new Money(registrationCurrency, registrationBonus);
        RegistrationBonusEventListener registrationBonusService = new RegistrationBonusEventListener(bonus, bonusService);
        notificationServiceListener.subscribe(registrationBonusService);
        return registrationBonusService;
    }

    @Bean
    @DependsOn("bonusService")
    public SocialAddedBonusEventListener socialAddedBonusEventListener(
            BonusService bonusService,
            SystemNotificationServiceListener notificationServiceListener,
            @Value("${clemble.bonus.social.currency}") Currency discoveryCurrency,
            @Value("${clemble.bonus.social.amount}") int discoveryBonus) {
        Money bonus = new Money(discoveryCurrency, discoveryBonus);
        SocialAddedBonusEventListener discoveryBonusService = new SocialAddedBonusEventListener(bonus, bonusService);
        notificationServiceListener.subscribe(discoveryBonusService);
        return discoveryBonusService;
    }

    @Bean
    public BonusPolicy bonusPolicy() {
        return new NoBonusPolicy();
    }

}
