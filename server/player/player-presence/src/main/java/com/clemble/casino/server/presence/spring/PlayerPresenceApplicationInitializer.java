package com.clemble.casino.server.presence.spring;

import com.clemble.casino.server.spring.AbstractWebApplicationInitializer;

/**
 * Created by mavarazy on 7/4/14.
 */
public class PlayerPresenceApplicationInitializer extends AbstractWebApplicationInitializer {

    public PlayerPresenceApplicationInitializer() {
        super(PlayerPresenceWebSpringConfiguration.class);
    }

}
