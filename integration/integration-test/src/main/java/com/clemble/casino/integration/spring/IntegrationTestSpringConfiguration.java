package com.clemble.casino.integration.spring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.configuration.MatchGameConfiguration;
import com.clemble.casino.game.configuration.RoundGameConfiguration;
import com.clemble.casino.rule.bet.UnlimitedBetRule;
import com.clemble.casino.game.rule.construct.PlayerNumberRule;
import com.clemble.casino.game.rule.giveup.GiveUpRule;
import com.clemble.casino.game.rule.match.MatchFillRule;
import com.clemble.casino.game.rule.outcome.DrawRule;
import com.clemble.casino.game.rule.outcome.WonRule;
import com.clemble.casino.rule.time.MoveTimeRule;
import com.clemble.casino.rule.time.TotalTimeRule;
import com.clemble.casino.game.rule.visibility.VisibilityRule;
import com.clemble.casino.game.unit.GameUnit;
import com.clemble.casino.goal.spring.GoalJudgeDutySpringConfiguration;
import com.clemble.casino.goal.spring.GoalJudgeSpringConfiguration;
import com.clemble.casino.integration.player.ClembleCasinoRegistrationOperationsWrapper;
import com.clemble.casino.json.ObjectMapperUtils;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.registration.PlayerToken;
import com.clemble.casino.registration.service.PlayerManualRegistrationService;
import com.clemble.casino.registration.service.PlayerSocialRegistrationService;
import com.clemble.casino.registration.PlayerLoginRequest;
import com.clemble.casino.registration.PlayerRegistrationRequest;
import com.clemble.casino.registration.PlayerSocialGrantRegistrationRequest;
import com.clemble.casino.registration.PlayerSocialRegistrationRequest;
import com.clemble.casino.rule.breach.LooseBreachPunishment;
import com.clemble.casino.rule.privacy.PrivacyRule;
import com.clemble.casino.server.event.SystemEvent;
import com.clemble.casino.server.game.configuration.ServerGameConfiguration;
import com.clemble.casino.server.game.configuration.repository.ServerGameConfigurationRepository;
import com.clemble.casino.server.game.configuration.spring.GameConfigurationSpringConfiguration;
import com.clemble.casino.server.game.construction.spring.GameConstructionSpringConfiguration;
import com.clemble.casino.server.game.spring.GameManagementSpringConfiguration;
import com.clemble.casino.server.goal.spring.GoalSpringConfiguration;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.spring.WebJsonSpringConfiguration;
import com.clemble.casino.server.spring.common.PropertiesSpringConfiguration;
import com.clemble.casino.server.bonus.spring.PaymentBonusSpringConfiguration;
import com.clemble.casino.server.connection.spring.PlayerConnectionSpringConfiguration;
import com.clemble.casino.server.profile.spring.PlayerProfileSpringConfiguration;
import com.clemble.casino.server.social.spring.PlayerSocialSpringConfiguration;
import com.clemble.casino.server.registration.spring.RegistrationSpringConfiguration;
import com.clemble.casino.server.presence.spring.PlayerPresenceSpringConfiguration;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.google.common.collect.ImmutableList;
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
        GameConstructionSpringConfiguration.class,
        WebJsonSpringConfiguration.class // For testing MappingJackson2HttpMessageConverter
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
            // Step 1. Creating round configuration
            RoundGameConfiguration roundConfiguration = new RoundGameConfiguration(
                Game.num,
                "low",
                Money.create(Currency.FakeMoney, 50),
                UnlimitedBetRule.INSTANCE,
                GiveUpRule.all,
                new MoveTimeRule(2000, LooseBreachPunishment.getInstance()),
                new TotalTimeRule(4000, LooseBreachPunishment.getInstance()),
                PrivacyRule.everybody,
                PlayerNumberRule.two,
                VisibilityRule.visible,
                DrawRule.owned,
                WonRule.price,
                ImmutableList.of("A", "B"),
                new ArrayList<GameUnit>()
            );
            configurationRepository.save(new ServerGameConfiguration("low", roundConfiguration));
            // Step 2. Creating match configuration
            MatchGameConfiguration matchConfiguration = new MatchGameConfiguration(
                Game.pot,
                "pot",
                Money.create(Currency.FakeMoney, 200),
                PrivacyRule.everybody,
                PlayerNumberRule.two,
                MatchFillRule.none,
                new MoveTimeRule(50000, LooseBreachPunishment.getInstance()),
                new TotalTimeRule(500000, LooseBreachPunishment.getInstance()),
                WonRule.price,
                DrawRule.owned,
                ImmutableList.of(roundConfiguration, roundConfiguration, roundConfiguration),
                Collections.emptyList());
            configurationRepository.save(new ServerGameConfiguration("pot", matchConfiguration));
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
