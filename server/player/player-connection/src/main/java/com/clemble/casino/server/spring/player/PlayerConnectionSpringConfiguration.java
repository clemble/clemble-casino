package com.clemble.casino.server.spring.player;

import com.clemble.casino.server.repository.player.PlayerSocialNetworkRepository;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.web.player.PlayerConnectionController;
import org.springframework.context.annotation.*;

import com.clemble.casino.server.spring.common.BasicNeo4JSpringConfiguration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.repository.GraphRepository;

@Configuration
@Import(CommonSpringConfiguration.class)
public class PlayerConnectionSpringConfiguration extends BasicNeo4JSpringConfiguration {

    public PlayerConnectionSpringConfiguration(){
        setBasePackage("com.clemble.casino.server.player.social", "com.clemble.casino.server.game.pending");
    }

    @Override
    public String getFolder() {
        return "target/player_graph";
    }

    @Bean
    public PlayerConnectionController playerConnectionController(PlayerSocialNetworkRepository socialNetworkRepository) {
        return new PlayerConnectionController(socialNetworkRepository);
    }

}
