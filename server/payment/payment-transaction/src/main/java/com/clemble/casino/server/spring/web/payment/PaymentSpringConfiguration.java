package com.clemble.casino.server.spring.web.payment;

import com.clemble.casino.server.payment.listener.PaymentTransactionRequestEventListener;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.SystemNotificationServiceListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.server.player.account.ServerPlayerAccountService;
import com.clemble.casino.server.repository.payment.PaymentTransactionRepository;
import com.clemble.casino.server.repository.payment.PlayerAccountTemplate;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.payment.PaymentManagementSpringConfiguration;
import com.clemble.casino.server.web.payment.PaymentTransactionController;
import com.clemble.casino.server.web.player.account.PlayerAccountController;

@Configuration
@Import({PaymentManagementSpringConfiguration.class})
public class PaymentSpringConfiguration implements SpringConfiguration {

    @Bean
    public PaymentTransactionController paymentTransactionController(PaymentTransactionRepository paymentTransactionRepository) {
        return new PaymentTransactionController(paymentTransactionRepository);
    }

    @Bean
    public PlayerAccountController playerAccountController(
        PlayerAccountTemplate accountTemplate,
        ServerPlayerAccountService playerAccountService) {
        return new PlayerAccountController(playerAccountService, accountTemplate);
    }

    @Bean
    public PaymentTransactionRequestEventListener bonusService(
            PaymentTransactionRepository paymentTransactionRepository,
            PlayerAccountTemplate accountTemplate,
            SystemNotificationServiceListener notificationServiceListener,
            @Qualifier("playerNotificationService") PlayerNotificationService notificationService) {
        PaymentTransactionRequestEventListener eventListener = new PaymentTransactionRequestEventListener(paymentTransactionRepository, accountTemplate, notificationService);
        notificationServiceListener.subscribe(eventListener);
        return eventListener;
    }

}
