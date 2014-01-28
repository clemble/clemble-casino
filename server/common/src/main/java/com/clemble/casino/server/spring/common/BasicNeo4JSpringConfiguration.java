package com.clemble.casino.server.spring.common;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseBuilder;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.context.annotation.Bean;
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
import com.clemble.casino.server.converters.GameSessionKeyConverter;
import com.clemble.casino.server.error.ClembleConstraintExceptionResolver;
import com.google.common.collect.ImmutableMap;

@Configuration
@EnableNeo4jRepositories(basePackages = "com.clemble.casino.server.repository", includeFilters = { @Filter(value = GraphRepository.class, type = FilterType.ASSIGNABLE_TYPE) })
abstract public class BasicNeo4JSpringConfiguration extends Neo4jConfiguration implements SpringConfiguration {

    abstract public String getFolder();

    @Bean(destroyMethod = "shutdown")
    public GraphDatabaseService graphDatabaseService() {
        GraphDatabaseFactory databaseFactory = new GraphDatabaseFactory();
        GraphDatabaseBuilder graphDatabaseBuilder = databaseFactory.newEmbeddedDatabaseBuilder(getFolder());
        graphDatabaseBuilder.setConfig(ImmutableMap.<String, String> of("allow_store_upgrade", "true"));
        return graphDatabaseBuilder.newGraphDatabase();
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
        conversionService.addConverter(new GameSessionKeyConverter());
        return conversionService;
    }

}
