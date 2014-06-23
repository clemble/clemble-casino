package com.clemble.casino.server.spring.player;

import com.clemble.casino.server.spring.common.PlayerPresenceSpringConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;

@Configuration
@Import({
    PlayerMongoSpringConfiguration.class,
    PlayerNeo4JSpringConfiguration.class,
    CommonSpringConfiguration.class,
    PlayerPresenceSpringConfiguration.class,
//    ManagementJPASpringConfiguration.class
//    PlayerCouchbaseSpringConfiguration.class
})
public class PlayerManagementSpringConfiguration implements SpringConfiguration {

}
