package com.clemble.casino.goal.spring;

import com.clemble.casino.server.spring.WebCommonSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by mavarazy on 8/17/14.
 */
@Configuration
@Import({GoalJudgeSpringConfiguration.class, WebCommonSpringConfiguration.class })
public class GoalJudgeWebSpringConfiguration implements SpringConfiguration {
}
