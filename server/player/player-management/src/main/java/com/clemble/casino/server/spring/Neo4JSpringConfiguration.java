package com.clemble.casino.server.spring;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseBuilder;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.clemble.casino.server.error.ClembleConstraintExceptionResolver;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.google.common.collect.ImmutableMap;

@Configuration
@EnableNeo4jRepositories(basePackages = "com.clemble.casino.server.repository",
    includeFilters = { @Filter(value = GraphRepository.class, type = FilterType.ASSIGNABLE_TYPE) })
public class Neo4JSpringConfiguration extends Neo4jConfiguration implements SpringConfiguration {

    @Bean(destroyMethod = "shutdown")
    public GraphDatabaseService graphDatabaseService() {
        GraphDatabaseFactory databaseFactory = new GraphDatabaseFactory();
        GraphDatabaseBuilder graphDatabaseBuilder = databaseFactory.newEmbeddedDatabaseBuilder("target/graph_db");
        graphDatabaseBuilder.setConfig(ImmutableMap.<String, String>of("allow_store_upgrade", "true"));
        return graphDatabaseBuilder.newGraphDatabase();
    }

    @Bean
    @Override
    public PersistenceExceptionTranslator persistenceExceptionTranslator() {
        return new ClembleConstraintExceptionResolver();
    }


}
