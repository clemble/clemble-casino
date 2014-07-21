package com.clemble.casino.server.spring.payment;

import com.clemble.casino.server.repository.payment.PaymentTransactionRepository;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

import java.net.UnknownHostException;

/**
 * Created by mavarazy on 7/6/14.
 */
@Configuration
public class PaymentMongoSpringConfiguration implements SpringConfiguration {

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
