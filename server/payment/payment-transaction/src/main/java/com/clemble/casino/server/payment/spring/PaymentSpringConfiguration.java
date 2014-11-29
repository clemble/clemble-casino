package com.clemble.casino.server.payment.spring;

import com.clemble.casino.error.ClembleCasinoValidationService;
import com.clemble.casino.server.payment.account.BasicServerPlayerAccountService;
import com.clemble.casino.server.payment.listener.SystemPaymentFreezeRequestEventListener;
import com.clemble.casino.server.payment.listener.SystemPaymentTransactionRequestEventListener;
import com.clemble.casino.server.payment.listener.SystemPlayerAccountCreationEventListener;
import com.clemble.casino.server.payment.repository.*;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.RedisSpringConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.server.payment.account.ServerPlayerAccountService;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.payment.controller.PaymentTransactionServiceController;
import com.clemble.casino.server.payment.controller.PlayerAccountServiceController;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import redis.clients.jedis.JedisPool;

@Configuration
@Import({PaymentSpringConfiguration.PaymentManagementSpringConfiguration.class})
public class PaymentSpringConfiguration implements SpringConfiguration {

    @Bean
    public PaymentTransactionServiceController paymentTransactionController(PaymentTransactionRepository paymentTransactionRepository) {
        return new PaymentTransactionServiceController(paymentTransactionRepository);
    }

    @Bean
    public PlayerAccountServiceController playerAccountController(
        PlayerAccountTemplate accountTemplate,
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
    public PlayerAccountTemplate playerAccountTemplate(
        PlayerAccountRepository accountRepository,
        PendingTransactionRepository pendingTransactionRepository,
        @Qualifier("playerNotificationService") ServerNotificationService notificationService) {
        return new MongoPlayerAccountTemplate(accountRepository, pendingTransactionRepository, notificationService);
    }

    @Bean
    public ServerPlayerAccountService realPlayerAccountService(PlayerAccountTemplate playerAccountRepository) {
        return new BasicServerPlayerAccountService(playerAccountRepository);
    }

    @Bean
    public SystemPaymentTransactionRequestEventListener paymentTransactionRequestEventListener(
            PaymentTransactionRepository paymentTransactionRepository,
            PendingTransactionRepository pendingTransactionRepository,
            PlayerAccountTemplate accountTemplate,
            ClembleCasinoValidationService validationService,
            SystemNotificationServiceListener notificationServiceListener,
            @Qualifier("playerNotificationService") ServerNotificationService notificationService) {
        SystemPaymentTransactionRequestEventListener eventListener = new SystemPaymentTransactionRequestEventListener(
            paymentTransactionRepository,
            pendingTransactionRepository,
            accountTemplate,
            notificationService,
            validationService);
        notificationServiceListener.subscribe(eventListener);
        return eventListener;
    }

    @Bean
    public SystemPaymentFreezeRequestEventListener systemPaymentFreezeRequestEventListener(
        PlayerAccountTemplate accountTemplate,
        ClembleCasinoValidationService validationService,
        SystemNotificationServiceListener notificationServiceListener) {
        SystemPaymentFreezeRequestEventListener eventListener = new SystemPaymentFreezeRequestEventListener(accountTemplate, validationService);
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

    @Configuration
    @Import({
        CommonSpringConfiguration.class,
        PaymentMongoSpringConfiguration.class,
        RedisSpringConfiguration.class})
    public static class PaymentManagementSpringConfiguration implements SpringConfiguration {

        @Bean
        public PlayerAccountTemplate playerAccountTemplate(JedisPool jedisPool) {
            return new JedisPlayerAccountTemplate(jedisPool);
        }

        @Bean
        public ServerPlayerAccountService realPlayerAccountService(PlayerAccountTemplate playerAccountRepository) {
            return new BasicServerPlayerAccountService(playerAccountRepository);
        }

    }

}
