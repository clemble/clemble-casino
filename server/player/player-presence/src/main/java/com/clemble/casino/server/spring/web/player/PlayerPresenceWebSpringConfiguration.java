package com.clemble.casino.server.spring.web.player;

import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.web.WebCommonSpringConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by mavarazy on 7/5/14.
 */
@Configuration
@Import({PlayerPresenceSpringConfiguration.class, WebCommonSpringConfiguration.class})
public class PlayerPresenceWebSpringConfiguration implements SpringConfiguration {
}
