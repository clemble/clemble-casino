package com.clemble.casino.server.spring.common;

import com.clemble.casino.json.ObjectMapperUtils;
import com.clemble.casino.rule.breach.BreachPunishment;
import com.clemble.casino.server.rule.breach.BreachPunishmentToString;
import com.clemble.casino.server.rule.breach.StringToBreachPunishment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by mavarazy on 8/26/14.
 */
@Import(PropertiesSpringConfiguration.class)
public class MongoSpringConfiguration implements SpringConfiguration {

    @Bean
    public MongoTemplate mongoTemplate(@Value("${clemble.db.mongo.host}") String host, @Value("${clemble.db.mongo.port}") int port, @Value("${clemble.db.mongo.name}") String name) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(host, port);
        MongoTemplate template = new MongoTemplate(mongoClient, name);

        GenericConversionService conversionService = (GenericConversionService) template.getConverter().getConversionService();
        conversionService.addConverter(new StringToBreachPunishment());
        conversionService.addConverter(new BreachPunishmentToString());

        return template;
    }

    @Bean
    public MongoRepositoryFactory mongoRepositoryFactory(MongoTemplate mongoOperations) throws UnknownHostException {
        return new MongoRepositoryFactory(mongoOperations);
    }

}
