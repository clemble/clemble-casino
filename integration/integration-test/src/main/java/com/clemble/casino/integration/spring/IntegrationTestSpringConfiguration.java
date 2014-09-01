package com.clemble.casino.integration.spring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clemble.casino.game.configuration.GameConfiguration;
import com.clemble.casino.goal.spring.GoalJudgeDutySpringConfiguration;
import com.clemble.casino.goal.spring.GoalJudgeSpringConfiguration;
import com.clemble.casino.integration.player.ClembleCasinoRegistrationOperationsWrapper;
import com.clemble.casino.json.ObjectMapperUtils;
import com.clemble.casino.registration.PlayerToken;
import com.clemble.casino.registration.service.PlayerManualRegistrationService;
import com.clemble.casino.registration.service.PlayerSocialRegistrationService;
import com.clemble.casino.registration.PlayerLoginRequest;
import com.clemble.casino.registration.PlayerRegistrationRequest;
import com.clemble.casino.registration.PlayerSocialGrantRegistrationRequest;
import com.clemble.casino.registration.PlayerSocialRegistrationRequest;
import com.clemble.casino.server.event.SystemEvent;
import com.clemble.casino.server.game.configuration.ServerGameConfiguration;
import com.clemble.casino.server.game.configuration.repository.ServerGameConfigurationRepository;
import com.clemble.casino.server.game.configuration.spring.GameConfigurationSpringConfiguration;
import com.clemble.casino.server.game.construction.spring.GameConstructionSpringConfiguration;
import com.clemble.casino.server.game.spring.GameManagementSpringConfiguration;
import com.clemble.casino.server.goal.spring.GoalSpringConfiguration;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.common.PropertiesSpringConfiguration;
import com.clemble.casino.server.bonus.spring.PaymentBonusSpringConfiguration;
import com.clemble.casino.server.connection.spring.PlayerConnectionSpringConfiguration;
import com.clemble.casino.server.profile.spring.PlayerProfileSpringConfiguration;
import com.clemble.casino.server.social.spring.PlayerSocialSpringConfiguration;
import com.clemble.casino.server.registration.spring.RegistrationSpringConfiguration;
import com.clemble.casino.server.presence.spring.PlayerPresenceSpringConfiguration;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.clemble.casino.android.AndroidCasinoRegistrationTemplate;
import com.clemble.casino.android.player.AndroidPlayerFacadeRegistrationService;
import com.clemble.casino.client.ClembleCasinoRegistrationOperations;
import com.clemble.casino.client.error.ClembleCasinoResponseErrorHandler;
import com.clemble.casino.registration.service.PlayerFacadeRegistrationService;
import com.clemble.casino.server.spring.common.JsonSpringConfiguration;
import com.clemble.casino.server.spring.web.ClientRestCommonSpringConfiguration;
import com.clemble.casino.server.payment.spring.PaymentSpringConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.PostConstruct;

@Configuration
@Import(value = {
    BaseTestSpringConfiguration.class,
    JsonSpringConfiguration.class,
    IntegrationTestSpringConfiguration.LocalTestConfiguration.class,
    IntegrationTestSpringConfiguration.IntegrationTestConfiguration.class
})
public class IntegrationTestSpringConfiguration implements TestSpringConfiguration {

    @Configuration
    @Profile(DEFAULT)
    @Import({
        RegistrationSpringConfiguration.class,
        PlayerPresenceSpringConfiguration.class,
        PlayerConnectionSpringConfiguration.class,
        PlayerProfileSpringConfiguration.class,
        PlayerSocialSpringConfiguration.class,
        PaymentBonusSpringConfiguration.class,
        PaymentSpringConfiguration.class,
        GoalSpringConfiguration.class,
        GoalJudgeSpringConfiguration.class,
        GoalJudgeDutySpringConfiguration.class,
        GameManagementSpringConfiguration.class,
        GameConfigurationSpringConfiguration.class,
        GameConstructionSpringConfiguration.class
    })
    public static class LocalTestConfiguration {

        @Autowired
        public SystemNotificationServiceListener serviceListener;

        @Bean
        public PlayerFacadeRegistrationService playerFacadeRegistrationService(final PlayerManualRegistrationService registrationService, final PlayerSocialRegistrationService socialRegistrationService) {
            return new PlayerFacadeRegistrationService() {

                @Override
                public PlayerToken login(PlayerLoginRequest loginRequest) {
                    return registrationService.login(loginRequest);
                }

                @Override
                public PlayerToken createPlayer(PlayerRegistrationRequest registrationRequest) {
                    return registrationService.createPlayer(registrationRequest);
                }

                @Override
                public PlayerToken createSocialPlayer(PlayerSocialRegistrationRequest socialRegistrationRequest) {
                    return socialRegistrationService.createSocialPlayer(socialRegistrationRequest);
                }

                @Override
                public PlayerToken createSocialGrantPlayer(PlayerSocialGrantRegistrationRequest grantRegistrationRequest) {
                    return socialRegistrationService.createSocialGrantPlayer(grantRegistrationRequest);
                }

            };
        }

        @PostConstruct
        public void integrationSystemEventListener() {
            JsonSubTypes annotation = SystemEvent.class.getDeclaredAnnotation(JsonSubTypes.class);
            for(JsonSubTypes.Type type: annotation.value()){
                final String channel = type.name();
                serviceListener.subscribe(new SystemEventListener<SystemEvent>() {
                    @Override
                    public void onEvent(SystemEvent event) {
                        System.out.println("integration:system:listener " + event);
                    }

                    @Override
                    public String getChannel() {
                        return channel;
                    }

                    @Override
                    public String getQueueName() {
                        return "integration:" + channel;
                    }
                });
            }

        }

        @Autowired
        public ServerGameConfigurationRepository configurationRepository;

        @PostConstruct
        public void initConfigurations() throws IOException {
            ObjectMapper objectMapper = ObjectMapperUtils.OBJECT_MAPPER;
            // Step 1. Creating configuration key A
            String configurationKey = "low";
            GameConfiguration configuration = objectMapper.readValue("{\"type\":\"round\",\"game\":\"num\",\"configurationKey\":\"low\",\"price\":{\"currency\":\"FakeMoney\",\"amount\":50},\"betRule\":{\"betType\":\"unlimited\"},\"giveUpRule\":{\"giveUp\":\"all\"},\"moveTimeRule\":{\"rule\":\"moveTime\",\"limit\":2000,\"punishment\":\"loose\"},\"totalTimeRule\":{\"rule\":\"totalTime\",\"limit\":4000,\"punishment\":\"loose\"},\"privacyRule\":[\"privacy\",\"everybody\"],\"numberRule\":[\"participants\",\"two\"],\"visibilityRule\":\"visible\",\"drawRule\":[\"DrawRule\",\"owned\"],\"wonRule\":[\"WonRule\",\"price\"],\"roles\":[\"A\",\"B\"]}", GameConfiguration.class);
            configurationRepository.save(new ServerGameConfiguration(configurationKey, configuration));
            // Step 2. Creating configuration key B
            configurationKey = "pot";
            configuration = objectMapper.readValue("{\"drawRule\":[\"DrawRule\",\"owned\"],\"wonRule\":[\"WonRule\",\"price\"],\"type\":\"pot\",\"game\":\"pot\",\"configurationKey\":\"pot\",\"price\":{\"currency\":\"FakeMoney\",\"amount\":200},\"privacyRule\":[\"privacy\",\"everybody\"],\"numberRule\":[\"participants\",\"two\"],\"matchFillRule\":\"maxcommon\",\"moveTimeRule\":{\"rule\":\"moveTime\",\"limit\":50000,\"punishment\":\"loose\"},\"totalTimeRule\":{\"rule\":\"totalTime\",\"limit\":500000,\"punishment\":\"loose\"},\"configurations\":[{\"type\":\"round\",\"game\":\"num\",\"configurationKey\":\"low\",\"price\":{\"currency\":\"FakeMoney\",\"amount\":50},\"betRule\":{\"betType\":\"unlimited\"},\"giveUpRule\":{\"giveUp\":\"all\"},\"moveTimeRule\":{\"rule\":\"moveTime\",\"limit\":2000,\"punishment\":\"loose\"},\"totalTimeRule\":{\"rule\":\"totalTime\",\"limit\":4000,\"punishment\":\"loose\"},\"privacyRule\":[\"privacy\",\"everybody\"],\"numberRule\":[\"participants\",\"two\"],\"visibilityRule\":\"visible\",\"roles\":[\"A\",\"B\"]},{\"type\":\"round\",\"game\":\"num\",\"configurationKey\":\"low\",\"price\":{\"currency\":\"FakeMoney\",\"amount\":50},\"betRule\":{\"betType\":\"unlimited\"},\"giveUpRule\":{\"giveUp\":\"all\"},\"moveTimeRule\":{\"rule\":\"moveTime\",\"limit\":2000,\"punishment\":\"loose\"},\"totalTimeRule\":{\"rule\":\"totalTime\",\"limit\":4000,\"punishment\":\"loose\"},\"privacyRule\":[\"privacy\",\"everybody\"],\"numberRule\":[\"participants\",\"two\"],\"visibilityRule\":\"visible\",\"roles\":[\"A\",\"B\"]},{\"type\":\"round\",\"game\":\"num\",\"configurationKey\":\"low\",\"price\":{\"currency\":\"FakeMoney\",\"amount\":50},\"betRule\":{\"betType\":\"unlimited\"},\"giveUpRule\":{\"giveUp\":\"all\"},\"moveTimeRule\":{\"rule\":\"moveTime\",\"limit\":2000,\"punishment\":\"loose\"},\"totalTimeRule\":{\"rule\":\"totalTime\",\"limit\":4000,\"punishment\":\"loose\"},\"privacyRule\":[\"privacy\",\"everybody\"],\"numberRule\":[\"participants\",\"two\"],\"visibilityRule\":\"visible\",\"roles\":[\"A\",\"B\"]}]}", GameConfiguration.class);
            configurationRepository.save(new ServerGameConfiguration(configurationKey, configuration));
        }


    }

    @Configuration
    @Profile({ INTEGRATION_TEST, INTEGRATION_CLOUD, INTEGRATION_DEFAULT })
    @Import({PropertiesSpringConfiguration.class, ClientRestCommonSpringConfiguration.class })
    public static class IntegrationTestConfiguration {

        @Autowired
        @Qualifier("objectMapper")
        public ObjectMapper objectMapper;

        @Value("${clemble.host}")
        public String baseUrl;

        @Bean
        public RestTemplate restTemplate() {
            RestTemplate restTemplate = new RestTemplate();

            List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
            MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
            jackson2HttpMessageConverter.setObjectMapper(objectMapper);
            messageConverters.add(jackson2HttpMessageConverter);

            restTemplate.setMessageConverters(messageConverters);
            restTemplate.setErrorHandler(new ClembleCasinoResponseErrorHandler(objectMapper));
            return restTemplate;
        }

        @Bean
        public ClembleCasinoRegistrationOperations registrationOperations() {
            return new ClembleCasinoRegistrationOperationsWrapper(new AndroidCasinoRegistrationTemplate(baseUrl));
        }

        @Bean
        public PlayerFacadeRegistrationService playerRegistrationService() {
            return new AndroidPlayerFacadeRegistrationService(baseUrl);
        }

    }
}
