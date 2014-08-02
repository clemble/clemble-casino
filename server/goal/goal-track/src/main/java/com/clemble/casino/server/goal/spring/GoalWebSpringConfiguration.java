package com.clemble.casino.server.goal.spring;

import com.clemble.casino.server.spring.web.WebCommonSpringConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by mavarazy on 8/2/14.
 */
@Configuration
@Import({GoalSpringConfiguration.class, WebCommonSpringConfiguration.class})
public class GoalWebSpringConfiguration {
}
