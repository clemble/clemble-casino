package com.clemble.casino.server.spring.game;

import org.springframework.context.annotation.Configuration;

import com.clemble.casino.server.spring.common.BasicNeo4JSpringConfiguration;

@Configuration
public class GameNeo4jSpringConfiguration extends BasicNeo4JSpringConfiguration {

    public GameNeo4jSpringConfiguration(){
        setBasePackage("com.clemble.casino.server.game");
    }

    @Override
    public String getFolder() {
        return "target/game_graph";
    }

}
