package com.clemble.casino.server.spring.common;

import org.neo4j.graphdb.GraphDatabaseService;
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

@Configuration
@EnableNeo4jRepositories(basePackages = "com.clemble.casino.server.repository",
    includeFilters = { @Filter(value = GraphRepository.class, type = FilterType.ASSIGNABLE_TYPE) })
public class Neo4JSpringConfiguration extends Neo4jConfiguration implements SpringConfiguration {

    @Bean(destroyMethod = "shutdown")
    public GraphDatabaseService graphDatabaseService() {
        GraphDatabaseFactory databaseFactory = new GraphDatabaseFactory();
        return databaseFactory.newEmbeddedDatabase("target/graph.db");
    }

    @Bean
    @Override
    public PersistenceExceptionTranslator persistenceExceptionTranslator() {
        return new ClembleConstraintExceptionResolver();
    }


}
