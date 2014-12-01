package com.clemble.casino.server.post.spring;

import com.clemble.casino.server.spring.AbstractWebApplicationInitializer;

/**
 * Created by mavarazy on 11/30/14.
 */
public class PlayerFeedApplicationInitializer extends AbstractWebApplicationInitializer {

    public PlayerFeedApplicationInitializer() {
        super(PlayerFeedSpringConfiguration.class);
    }

}
