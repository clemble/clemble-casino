package com.clemble.casino.server.payment.spring;

import com.clemble.casino.error.ClembleCasinoValidationService;
import com.clemble.casino.server.payment.account.BasicServerPlayerAccountService;
import com.clemble.casino.server.payment.listener.SystemPaymentTransactionRequestEventListener;
import com.clemble.casino.server.payment.repository.JedisPlayerAccountTemplate;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.RedisSpringConfiguration;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.server.payment.account.ServerPlayerAccountService;
import com.clemble.casino.server.payment.repository.PaymentTransactionRepository;
import com.clemble.casino.server.payment.repository.PlayerAccountTemplate;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.payment.controller.PaymentTransactionServiceController;
import com.clemble.casino.server.payment.controller.PlayerAccountController;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import redis.clients.jedis.JedisPool;

import java.net.UnknownHostException;

@Configuration
@Import({PaymentSpringConfiguration.PaymentManagementSpringConfiguration.class})
public class PaymentSpringConfiguration implements SpringConfiguration {

    @Bean
    public PaymentTransactionServiceController paymentTransactionController(PaymentTransactionRepository paymentTransactionRepository) {
        return new PaymentTransactionServiceController(paymentTransactionRepository);
    }

    @Bean
    public PlayerAccountController playerAccountController(
        PlayerAccountTemplate accountTemplate,
        ServerPlayerAccountService playerAccountService) {
        return new PlayerAccountController(playerAccountService, accountTemplate);
    }

    @Bean
    public PlayerAccountTemplate playerAccountTemplate(JedisPool jedisPool) {
        return new JedisPlayerAccountTemplate(jedisPool);
    }

    @Bean
    public ServerPlayerAccountService realPlayerAccountService(PlayerAccountTemplate playerAccountRepository) {
        return new BasicServerPlayerAccountService(playerAccountRepository);
    }

    @Bean
    public SystemPaymentTransactionRequestEventListener paymentTransactionRequestEventListener(
            PaymentTransactionRepository paymentTransactionRepository,
            PlayerAccountTemplate accountTemplate,
            ClembleCasinoValidationService validationService,
            SystemNotificationServiceListener notificationServiceListener,
            @Qualifier("playerNotificationService") PlayerNotificationService notificationService) {
        SystemPaymentTransactionRequestEventListener eventListener = new SystemPaymentTransactionRequestEventListener(paymentTransactionRepository, accountTemplate, notificationService, validationService);
        notificationServiceListener.subscribe(eventListener);
        return eventListener;
    }

    @Configuration
    public static class PaymentMongoSpringConfiguration implements SpringConfiguration {

        @Bean
        public MongoRepositoryFactory paymentRepositoryFactory(@Value("${clemble.db.mongo.host}") String host, @Value("${clemble.db.mongo.port}") int port) throws UnknownHostException {
            MongoClient mongoClient = new MongoClient(host, port);
            MongoOperations mongoOperations = new MongoTemplate(mongoClient, "clemble");
            return new MongoRepositoryFactory(mongoOperations);
        }

        @Bean
        public PaymentTransactionRepository paymentTransactionRepository(@Qualifier("paymentRepositoryFactory") MongoRepositoryFactory paymentRepositoryFactory) {
            return paymentRepositoryFactory.getRepository(PaymentTransactionRepository.class);
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
