package com.clemble.casino.server.profile.spring;

import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.web.WebCommonSpringConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by mavarazy on 7/5/14.
 */
@Configuration
@Import({PlayerProfileSpringConfiguration.class, WebCommonSpringConfiguration.class})
public class PlayerProfileWebSpringConfiguration implements SpringConfiguration {
}
