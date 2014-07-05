package com.clemble.casino.server.spring.player;

import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.web.WebCommonSpringConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by mavarazy on 7/5/14.
 */
@Configuration
@Import({PlayerConnectionSpringConfiguration.class, WebCommonSpringConfiguration.class})
public class PlayerConnectionWebSpringConfiguration implements SpringConfiguration {
}
