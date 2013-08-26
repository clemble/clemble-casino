package com.gogomaya.server.spring.integration;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.server.game.Game;
import com.gogomaya.server.integration.game.GameSessionPlayerFactory;
import com.gogomaya.server.integration.game.IntegrationGameSessionPlayerFactory;
import com.gogomaya.server.integration.game.WebGameSessionPlayerFactory;
import com.gogomaya.server.integration.game.construction.GameConstructionOperations;
import com.gogomaya.server.integration.game.construction.IntegrationGameConstructionOperations;
import com.gogomaya.server.integration.game.construction.WebGameConstructionOperations;
import com.gogomaya.server.integration.game.tictactoe.PicPacPoePlayerSessionFactory;
import com.gogomaya.server.spring.common.JsonSpringConfiguration;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.web.ClientRestCommonSpringConfiguration;
import com.gogomaya.server.spring.web.PicPacPoeWebSpringConfiguration;
import com.gogomaya.server.tictactoe.PicPacPoeState;
import com.gogomaya.server.web.error.GogomayaRESTErrorHandler;
import com.gogomaya.server.web.game.options.GameConfigurationManagerController;
import com.gogomaya.server.web.game.session.GameActionController;
import com.gogomaya.server.web.game.session.GameConstructionController;

@Configuration
@Import(value = { JsonSpringConfiguration.class,
        TestConfiguration.class,
        PicPacPoeTestConfiguration.LocalTestConfiguration.class,
        PicPacPoeTestConfiguration.LocalIntegrationTestConfiguration.class,
        PicPacPoeTestConfiguration.RemoteIntegrationTestConfiguration.class })
public class PicPacPoeTestConfiguration {

    @Configuration
    @Profile(value = SpringConfiguration.DEFAULT)
    @Import(value = { PicPacPoeWebSpringConfiguration.class })
    public static class LocalTestConfiguration {

        @Autowired
        @Qualifier("objectMapper")
        public ObjectMapper objectMapper;

        @Autowired
        @Qualifier("picPacPoeConfigurationManagerController")
        public GameConfigurationManagerController ticTacToeConfigurationManagerController;

        @Autowired
        @Qualifier("picPacPoeConstructionController")
        public GameConstructionController<PicPacPoeState> ticTacToeConstructionController;

        @Autowired
        @Qualifier("picPacPoeEngineController")
        public GameActionController<PicPacPoeState> ticTacToeEngineController;

        @Bean
        @Singleton
        public GameSessionPlayerFactory<PicPacPoeState> picPacPoeSessionFactory() {
            return new WebGameSessionPlayerFactory<PicPacPoeState>(ticTacToeEngineController, ticTacToeConstructionController);
        }

        @Bean
        @Singleton
        public GameConstructionOperations<PicPacPoeState> picPacPoeGameConstructionOperations() {
            return new WebGameConstructionOperations<PicPacPoeState>(Game.pic, ticTacToeConfigurationManagerController, ticTacToeConstructionController,
                    picPacPoeSessionPlayerFactory());
        }

        @Bean
        @Singleton
        public GameSessionPlayerFactory<PicPacPoeState> picPacPoeSessionPlayerFactory() {
            return new PicPacPoePlayerSessionFactory(picPacPoeSessionFactory());
        }

    }

    @Configuration
    @Profile(value = SpringConfiguration.INTEGRATION_CLOUD)
    public static class RemoteIntegrationTestConfiguration extends IntegrationTestConfiguration {

        @Override
        public String getBaseUrl() {
            return "http://ec2-50-16-93-157.compute-1.amazonaws.com/gogomaya/";
        }

    }

    @Configuration
    @Profile(value = SpringConfiguration.INTEGRATION_TEST)
    public static class LocalIntegrationTestConfiguration extends IntegrationTestConfiguration {

        @Override
        public String getBaseUrl() {
            return "http://localhost:9999/";
        }

    }

    @Configuration
    @Profile(value = SpringConfiguration.INTEGRATION_DEFAULT)
    public static class LocalServerIntegrationTestConfiguration extends IntegrationTestConfiguration {

    }

    @Import(ClientRestCommonSpringConfiguration.class)
    public static class IntegrationTestConfiguration {

        @Autowired
        @Qualifier("objectMapper")
        public ObjectMapper objectMapper;

        public String getBaseUrl() {
            return "http://localhost:8080/";
        }

        @Bean
        @Singleton
        public RestTemplate restTemplate() {
            RestTemplate restTemplate = new RestTemplate();

            for (HttpMessageConverter<?> messageConverter : restTemplate.getMessageConverters()) {
                if (messageConverter instanceof MappingJackson2HttpMessageConverter) {
                    ((MappingJackson2HttpMessageConverter) messageConverter).setObjectMapper(objectMapper);
                }
            }

            restTemplate.setErrorHandler(new GogomayaRESTErrorHandler(objectMapper));
            return restTemplate;
        }

        @Bean
        @Singleton
        public GameConstructionOperations<PicPacPoeState> picPacPoeGameConstructionOperations() {
            return new IntegrationGameConstructionOperations<PicPacPoeState>(Game.pic, getBaseUrl(), restTemplate(), picPacPoeSessionPlayerFactory());
        }

        @Bean
        @Singleton
        public GameSessionPlayerFactory<PicPacPoeState> picPacPoeSessionPlayerFactory() {
            return new PicPacPoePlayerSessionFactory(new IntegrationGameSessionPlayerFactory<PicPacPoeState>(restTemplate(), getBaseUrl()));
        }

    }
}
