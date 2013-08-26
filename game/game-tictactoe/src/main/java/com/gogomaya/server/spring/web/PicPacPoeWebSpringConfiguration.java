package com.gogomaya.server.spring.web;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import com.gogomaya.server.game.action.GameSessionProcessor;
import com.gogomaya.server.game.configuration.GameSpecificationRegistry;
import com.gogomaya.server.game.construct.GameConstructionService;
import com.gogomaya.server.repository.game.GameConstructionRepository;
import com.gogomaya.server.repository.game.GameSessionRepository;
import com.gogomaya.server.repository.game.GameTableRepository;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.tictactoe.PicPacPoeSpringConfiguration;
import com.gogomaya.server.spring.web.PicPacPoeWebSpringConfiguration.GameDefaultAndTest;
import com.gogomaya.server.tictactoe.PicPacPoeState;
import com.gogomaya.server.web.game.options.GameConfigurationManagerController;
import com.gogomaya.server.web.game.session.GameActionController;
import com.gogomaya.server.web.game.session.GameConstructionController;
import com.mangofactory.swagger.SwaggerConfiguration;
import com.mangofactory.swagger.configuration.DefaultConfigurationModule;
import com.mangofactory.swagger.configuration.ExtensibilityModule;

@Configuration
@Import(value = { PicPacPoeSpringConfiguration.class, WebCommonSpringConfiguration.class, GameDefaultAndTest.class })
public class PicPacPoeWebSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("gameSessionRepository")
    public GameSessionRepository<PicPacPoeState> gameSessionRepository;

    @Autowired
    @Qualifier("gameTableRepository")
    public GameTableRepository<PicPacPoeState> tableRepository;

    @Autowired
    @Qualifier("gameConstructionRepository")
    public GameConstructionRepository constructionRepository;

    @Autowired
    @Qualifier("picPacPoeConstructionService")
    public GameConstructionService constructionService;

    @Autowired
    @Qualifier("picPacPoeSessionProcessor")
    public GameSessionProcessor<PicPacPoeState> picPacPoeSessionProcessor;

    @Autowired
    public GameSpecificationRegistry gameSpecificationRegistry;

    @Bean
    @Singleton
    public GameConstructionController<PicPacPoeState> picPacPoeConstructionController() {
        return new GameConstructionController<PicPacPoeState>(constructionRepository, constructionService, gameSpecificationRegistry);
    }

    @Bean
    @Singleton
    public GameConfigurationManagerController picPacPoeConfigurationManagerController() {
        return new GameConfigurationManagerController(gameSpecificationRegistry);
    }

    @Bean
    @Singleton
    public GameActionController<PicPacPoeState> picPacPoeEngineController() {
        return new GameActionController<PicPacPoeState>(gameSessionRepository, picPacPoeSessionProcessor);
    }

    @Configuration
    @Profile(value = { DEFAULT, UNIT_TEST, INTEGRATION_TEST })
    public static class GameDefaultAndTest extends SwaggerSpringConfiguration {

        @Override
        public SwaggerConfiguration swaggerConfiguration(DefaultConfigurationModule defaultConfig, ExtensibilityModule extensibility) {
            SwaggerConfiguration swaggerConfiguration = new SwaggerConfiguration("1.0", "http://localhost:8080/game-web/");
            return extensibility.apply(defaultConfig.apply(swaggerConfiguration));
        }

    }

}
