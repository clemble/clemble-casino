package com.clemble.casino.server.spring.common;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

import java.net.UnknownHostException;

/**
 * Created by mavarazy on 8/26/14.
 */
@Import(PropertiesSpringConfiguration.class)
public class MongoSpringConfiguration implements SpringConfiguration {

    @Bean
    public MongoTemplate mongoTemplate(@Value("${clemble.db.mongo.host}") String host, @Value("${clemble.db.mongo.port}") int port, @Value("${clemble.db.mongo.name}") String name) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(host, port);
        return new MongoTemplate(mongoClient, name);
    }

    @Bean
    public MongoRepositoryFactory mongoRepositoryFactory(MongoTemplate mongoOperations) throws UnknownHostException {
        return new MongoRepositoryFactory(mongoOperations);
    }

}
