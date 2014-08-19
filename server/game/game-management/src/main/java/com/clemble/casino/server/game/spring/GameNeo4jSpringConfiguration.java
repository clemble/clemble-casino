package com.clemble.casino.server.game.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.clemble.casino.server.spring.common.BasicNeo4JSpringConfiguration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.repository.GraphRepository;

@Configuration
@EnableNeo4jRepositories(basePackages = "com.clemble.casino.server.game.repository", includeFilters = { @ComponentScan.Filter(value = GraphRepository.class, type = FilterType.ASSIGNABLE_TYPE) })
public class GameNeo4jSpringConfiguration extends BasicNeo4JSpringConfiguration {

    public GameNeo4jSpringConfiguration(){
        setBasePackage("com.clemble.casino.server.player.social", "com.clemble.casino.server.game.pending");
    }

    @Override
    public String getFolder() {
        return "target/game_graph";
    }

}
