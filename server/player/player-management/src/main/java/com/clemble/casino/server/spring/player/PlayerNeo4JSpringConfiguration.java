package com.clemble.casino.server.spring.player;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.clemble.casino.server.spring.common.BasicNeo4JSpringConfiguration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.repository.GraphRepository;

@Configuration
public class PlayerNeo4JSpringConfiguration extends BasicNeo4JSpringConfiguration {

    public PlayerNeo4JSpringConfiguration(){
        setBasePackage("com.clemble.casino.server.player.social");
    }

    @Override
    public String getFolder() {
        return "target/player_graph";
    }

}
