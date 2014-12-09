package com.clemble.casino.server.phone.spring;

import com.clemble.casino.server.spring.AbstractWebApplicationInitializer;

/**
 * Created by mavarazy on 12/9/14.
 */
public class PlayerPhoneApplicationInitializer extends AbstractWebApplicationInitializer {

    public PlayerPhoneApplicationInitializer() {
        super(PlayerPhoneSpringConfiguration.class);
    }

}
