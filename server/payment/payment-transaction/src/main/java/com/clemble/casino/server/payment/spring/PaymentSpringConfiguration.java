package com.clemble.casino.server.payment.spring;

import com.clemble.casino.server.payment.account.BasicServerPlayerAccountService;
import com.clemble.casino.server.payment.listener.SystemPaymentFreezeRequestEventListener;
import com.clemble.casino.server.payment.listener.SystemPaymentTransactionRequestEventListener;
import com.clemble.casino.server.payment.listener.SystemPlayerAccountCreationEventListener;
import com.clemble.casino.server.payment.repository.*;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.server.payment.account.ServerPlayerAccountService;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.payment.controller.PaymentTransactionServiceController;
import com.clemble.casino.server.payment.controller.PlayerAccountServiceController;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

@Configuration
@Import(CommonSpringConfiguration.class)
public class PaymentSpringConfiguration implements SpringConfiguration {

    @Bean
    public PaymentTransactionServiceController paymentTransactionController(PaymentTransactionRepository paymentTransactionRepository) {
        return new PaymentTransactionServiceController(paymentTransactionRepository);
    }

    @Bean
    public PlayerAccountServiceController playerAccountController(
        ServerAccountService accountTemplate,
        ServerPlayerAccountService playerAccountService) {
        return new PlayerAccountServiceController(playerAccountService, accountTemplate);
    }

    @Bean
    public PlayerAccountRepository playerAccountRepository(MongoRepositoryFactory repositoryFactory){
        return repositoryFactory.getRepository(PlayerAccountRepository.class);
    }

    @Bean
    public PendingTransactionRepository pendingTransactionRepository(MongoRepositoryFactory repositoryFactory) {
        return repositoryFactory.getRepository(PendingTransactionRepository.class);
    }

    @Bean
    public ServerAccountService playerAccountTemplate(
        PlayerAccountRepository accountRepository,
        PaymentTransactionRepository transactionRepository,
        PendingTransactionRepository pendingTransactionRepository,
        @Qualifier("playerNotificationService") ServerNotificationService notificationService) {
        return new MongoServerAccountService(accountRepository, transactionRepository, pendingTransactionRepository, notificationService);
    }

    @Bean
    public ServerPlayerAccountService realPlayerAccountService(ServerAccountService playerAccountRepository) {
        return new BasicServerPlayerAccountService(playerAccountRepository);
    }

    @Bean
    public SystemPaymentTransactionRequestEventListener paymentTransactionRequestEventListener(
            ServerAccountService accountTemplate,
            SystemNotificationServiceListener notificationServiceListener) {
        SystemPaymentTransactionRequestEventListener eventListener = new SystemPaymentTransactionRequestEventListener(accountTemplate);
        notificationServiceListener.subscribe(eventListener);
        return eventListener;
    }

    @Bean
    public SystemPaymentFreezeRequestEventListener systemPaymentFreezeRequestEventListener(
        ServerAccountService accountTemplate,
        SystemNotificationServiceListener notificationServiceListener) {
        SystemPaymentFreezeRequestEventListener eventListener = new SystemPaymentFreezeRequestEventListener(accountTemplate);
        notificationServiceListener.subscribe(eventListener);
        return eventListener;
    }

    @Bean
    public SystemPlayerAccountCreationEventListener systemPlayerAccountCreationEventListener(
            PlayerAccountRepository accountRepository,
            SystemNotificationServiceListener notificationServiceListener) {
        SystemPlayerAccountCreationEventListener eventListener = new SystemPlayerAccountCreationEventListener(accountRepository);
        notificationServiceListener.subscribe(eventListener);
        return eventListener;
    }

    @Configuration
    @Import(MongoSpringConfiguration.class)
    public static class PaymentMongoSpringConfiguration implements SpringConfiguration {

        @Bean
        public PaymentTransactionRepository paymentTransactionRepository(MongoRepositoryFactory mongoRepositoryFactory) {
            return mongoRepositoryFactory.getRepository(PaymentTransactionRepository.class);
        }

    }

}
