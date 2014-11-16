package com.clemble.casino.server.spring.common;

import com.clemble.casino.game.lifecycle.management.GameState;
import com.clemble.casino.server.aspect.notification.PlayerNotificationRuleAspectFactory;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by mavarazy on 11/16/14.
 */
@Configuration
public class CommonManagementSpringConfiguration implements SpringConfiguration {

    @Bean
    public PlayerNotificationRuleAspectFactory gameNotificationManagementAspectFactory(PlayerNotificationService playerNotificationService) {
        return new PlayerNotificationRuleAspectFactory<GameState>(playerNotificationService);
    }

}
