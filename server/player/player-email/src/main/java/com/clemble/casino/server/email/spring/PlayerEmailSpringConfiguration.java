package com.clemble.casino.server.email.spring;

import com.clemble.casino.server.email.PlayerEmail;
import com.clemble.casino.server.email.listener.SystemEmailAddedEventListener;
import com.clemble.casino.server.email.repository.PlayerEmailRepository;
import com.clemble.casino.server.email.service.MandrillPlayerEmailService;
import com.clemble.casino.server.email.service.PlayerEmailService;
import com.clemble.casino.server.event.email.SystemEmailAddedEvent;
import com.clemble.casino.server.event.email.SystemEmailVerifiedEvent;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillUserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

import java.io.IOException;

/**
 * Created by mavarazy on 12/6/14.
 */
@Configuration
@Import({CommonSpringConfiguration.class, MongoSpringConfiguration.class, PlayerEmailSpringConfiguration.Cloud.class, PlayerEmailSpringConfiguration.Default.class} )
public class PlayerEmailSpringConfiguration implements SpringConfiguration {

    @Bean
    public PlayerEmailRepository playerEmailRepository(MongoRepositoryFactory repositoryFactory) {
        return repositoryFactory.getRepository(PlayerEmailRepository.class);
    }

    @Bean
    public SystemEmailAddedEventListener systemEmailAddedEventListener(
        PlayerEmailRepository emailRepository,
        PlayerEmailService playerEmailService,
        SystemNotificationService systemNotificationService,
        SystemNotificationServiceListener notificationServiceListener) {
        SystemEmailAddedEventListener emailAddedEventListener = new SystemEmailAddedEventListener(playerEmailService, emailRepository, systemNotificationService);
        notificationServiceListener.subscribe(emailAddedEventListener);
        return emailAddedEventListener;
    }

    @Configuration
    @Profile({SpringConfiguration.TEST, SpringConfiguration.DEFAULT, SpringConfiguration.INTEGRATION_TEST})
    public static class Default implements SpringConfiguration {

        @Bean
        public PlayerEmailService playerEmailService(final SystemNotificationService notificationService) {
            return new PlayerEmailService() {
                @Override
                public void requestVerification(PlayerEmail email) {
                    notificationService.send(new SystemEmailVerifiedEvent(email.getPlayer()));
                }
            };
        }

    }

    @Configuration
    @Profile(SpringConfiguration.CLOUD)
    public static class Cloud implements SpringConfiguration {

        @Bean
        public MandrillApi mandrillApi(@Value("${clemble.email.mandrill.key}") String apiKey) throws IOException, MandrillApiError {
            MandrillApi mandrillApi = new MandrillApi(apiKey);
            MandrillUserInfo user = mandrillApi.users().info();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println(gson.toJson(user));

            return mandrillApi;
        }

        @Bean
        public MandrillPlayerEmailService mandrillPlayerEmailService(MandrillApi mandrillApi) {
            return new MandrillPlayerEmailService(mandrillApi);
        }

    }

}
