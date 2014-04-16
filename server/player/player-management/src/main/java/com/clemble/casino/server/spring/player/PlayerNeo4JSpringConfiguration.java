package com.clemble.casino.server.spring.player;

import org.springframework.context.annotation.Configuration;

import com.clemble.casino.server.spring.common.BasicNeo4JSpringConfiguration;

@Configuration
public class PlayerNeo4JSpringConfiguration extends BasicNeo4JSpringConfiguration {

    public PlayerNeo4JSpringConfiguration(){
    }

    @Override
    public String getFolder() {
        return "target/player_graph";
    }

}
