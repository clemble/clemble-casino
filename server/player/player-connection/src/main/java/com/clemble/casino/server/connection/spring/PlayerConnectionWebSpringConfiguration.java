package com.clemble.casino.server.connection.spring;

import com.clemble.casino.server.spring.WebBasicSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by mavarazy on 7/5/14.
 */
@Configuration
@Import({PlayerConnectionSpringConfiguration.class, WebBasicSpringConfiguration.class})
public class PlayerConnectionWebSpringConfiguration implements SpringConfiguration {
}
