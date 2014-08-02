package com.clemble.casino.server.social.spring;

import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.WebCommonSpringConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by mavarazy on 7/5/14.
 */
@Configuration
@Import({PlayerSocialSpringConfiguration.class, WebCommonSpringConfiguration.class})
public class PlayerSocialWebSpringConfiguration implements SpringConfiguration {
}
