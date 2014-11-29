package com.clemble.casino.server.notification.spring;

import com.clemble.casino.server.spring.AbstractWebApplicationInitializer;

/**
 * Created by mavarazy on 11/29/14.
 */
public class PlayerNotificationApplicationInitializer extends AbstractWebApplicationInitializer {

    public PlayerNotificationApplicationInitializer() {
        super(PlayerNotificationSpringConfiguration.class);
    }

}
