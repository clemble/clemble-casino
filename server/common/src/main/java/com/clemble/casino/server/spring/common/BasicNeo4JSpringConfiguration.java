package com.clemble.casino.server.spring.common;

import com.clemble.casino.server.converters.GameConfigurationConverter;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.config.Setting;
import org.neo4j.graphdb.factory.GraphDatabaseBuilder;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.clemble.casino.server.converters.GameConfigurationKeyConverter;
import com.clemble.casino.server.error.ClembleConstraintExceptionResolver;
import com.google.common.collect.ImmutableMap;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;

@Configuration
@EnableNeo4jRepositories(basePackages = "com.clemble.casino.server.repository", includeFilters = { @ComponentScan.Filter(value = GraphRepository.class, type = FilterType.ASSIGNABLE_TYPE) })
abstract public class BasicNeo4JSpringConfiguration extends Neo4jConfiguration implements SpringConfiguration {

    abstract public String getFolder();

    @Bean(destroyMethod = "shutdown")
    public GraphDatabaseService graphDatabaseService(
        @Value("${clemble.db.neo4j.url}") String url,
        @Value("${clemble.db.neo4j.user}") String user,
        @Value("${clemble.db.neo4j.password}") String password) {
        SpringRestGraphDatabase graphDatabase = new SpringRestGraphDatabase(url, user, password);
        return graphDatabase;
    }

    @Bean
    @Override
    public PersistenceExceptionTranslator persistenceExceptionTranslator() {
        return new ClembleConstraintExceptionResolver();
    }

    @Override
    @Bean
    protected ConversionService neo4jConversionService() throws Exception {
        GenericConversionService conversionService = (GenericConversionService) super.neo4jConversionService();
        conversionService.addConverter(new GameConfigurationKeyConverter());
        conversionService.addConverter(new GameConfigurationConverter());
        return conversionService;
    }

}
