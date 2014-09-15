package com.clemble.casino.goal.configuration.spring;

import com.clemble.casino.goal.configuration.controller.GoalConfigurationServiceController;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by mavarazy on 9/15/14.
 */
@Configuration
public class GoalConfigurationSpringConfiguration implements SpringConfiguration {

    @Bean
    public GoalConfigurationServiceController goalConfigurationServiceController(){
        return new GoalConfigurationServiceController();
    }

}
