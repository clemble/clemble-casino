package com.clemble.casino.integration.game.spring;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.configuration.ServerGameConfiguration;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.game.specification.GameConfigurationKey;
import com.clemble.casino.json.ObjectMapperUtils;
import com.clemble.casino.server.game.repository.ServerGameConfigurationRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.integration.game.NumberState;
import com.clemble.casino.integration.game.NumberStateFactory;
import com.clemble.casino.game.GameStateFactory;
import com.clemble.casino.server.game.spring.GameManagementSpringConfiguration;
import com.clemble.casino.server.spring.web.game.AbstractGameSpringConfiguration;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
@Import({ GameManagementSpringConfiguration.class})
public class IntegrationGameSpringConfiguration extends AbstractGameSpringConfiguration<NumberState> {


    @Autowired
    public ServerGameConfigurationRepository configurationRepository;

    @PostConstruct
    public void initConfigurations() throws IOException {
        ObjectMapper objectMapper = ObjectMapperUtils.OBJECT_MAPPER;
        // Step 1. Creating configuration key A
        GameConfigurationKey configurationKey = new GameConfigurationKey(Game.num, "low");
        GameConfiguration configuration = objectMapper.readValue("{\"type\":\"round\",\"configurationKey\":{\"game\":\"num\",\"specificationName\":\"low\"},\"price\":{\"currency\":\"FakeMoney\",\"amount\":50},\"betRule\":{\"betType\":\"unlimited\"},\"giveUpRule\":{\"giveUp\":\"all\"},\"moveTimeRule\":{\"rule\":\"moveTime\",\"limit\":2000,\"punishment\":\"loose\"},\"totalTimeRule\":{\"rule\":\"totalTime\",\"limit\":4000,\"punishment\":\"loose\"},\"privacyRule\":[\"privacy\",\"everybody\"],\"numberRule\":[\"participants\",\"two\"],\"visibilityRule\":\"visible\",\"drawRule\":[\"DrawRule\",\"owned\"],\"wonRule\":[\"WonRule\",\"price\"],\"roles\":[\"A\",\"B\"]}", GameConfiguration.class);
        configurationRepository.save(new ServerGameConfiguration(configurationKey, configuration));
        // Step 2. Creating configuration key B
        configurationKey = new GameConfigurationKey(Game.pot, "pot");
        configuration = objectMapper.readValue("{\"drawRule\":[\"DrawRule\",\"owned\"],\"wonRule\":[\"WonRule\",\"price\"],\"type\":\"pot\",\"configurationKey\":{\"game\":\"pot\",\"specificationName\":\"pot\"},\"price\":{\"currency\":\"FakeMoney\",\"amount\":200},\"privacyRule\":[\"privacy\",\"everybody\"],\"numberRule\":[\"participants\",\"two\"],\"matchFillRule\":\"maxcommon\",\"moveTimeRule\":{\"rule\":\"moveTime\",\"limit\":50000,\"punishment\":\"loose\"},\"totalTimeRule\":{\"rule\":\"totalTime\",\"limit\":500000,\"punishment\":\"loose\"},\"configurations\":[{\"type\":\"round\",\"configurationKey\":{\"game\":\"num\",\"specificationName\":\"low\"},\"price\":{\"currency\":\"FakeMoney\",\"amount\":50},\"betRule\":{\"betType\":\"unlimited\"},\"giveUpRule\":{\"giveUp\":\"all\"},\"moveTimeRule\":{\"rule\":\"moveTime\",\"limit\":2000,\"punishment\":\"loose\"},\"totalTimeRule\":{\"rule\":\"totalTime\",\"limit\":4000,\"punishment\":\"loose\"},\"privacyRule\":[\"privacy\",\"everybody\"],\"numberRule\":[\"participants\",\"two\"],\"visibilityRule\":\"visible\",\"roles\":[\"A\",\"B\"]},{\"type\":\"round\",\"configurationKey\":{\"game\":\"num\",\"specificationName\":\"low\"},\"price\":{\"currency\":\"FakeMoney\",\"amount\":50},\"betRule\":{\"betType\":\"unlimited\"},\"giveUpRule\":{\"giveUp\":\"all\"},\"moveTimeRule\":{\"rule\":\"moveTime\",\"limit\":2000,\"punishment\":\"loose\"},\"totalTimeRule\":{\"rule\":\"totalTime\",\"limit\":4000,\"punishment\":\"loose\"},\"privacyRule\":[\"privacy\",\"everybody\"],\"numberRule\":[\"participants\",\"two\"],\"visibilityRule\":\"visible\",\"roles\":[\"A\",\"B\"]},{\"type\":\"round\",\"configurationKey\":{\"game\":\"num\",\"specificationName\":\"low\"},\"price\":{\"currency\":\"FakeMoney\",\"amount\":50},\"betRule\":{\"betType\":\"unlimited\"},\"giveUpRule\":{\"giveUp\":\"all\"},\"moveTimeRule\":{\"rule\":\"moveTime\",\"limit\":2000,\"punishment\":\"loose\"},\"totalTimeRule\":{\"rule\":\"totalTime\",\"limit\":4000,\"punishment\":\"loose\"},\"privacyRule\":[\"privacy\",\"everybody\"],\"numberRule\":[\"participants\",\"two\"],\"visibilityRule\":\"visible\",\"roles\":[\"A\",\"B\"]}]}", GameConfiguration.class);
        configurationRepository.save(new ServerGameConfiguration(configurationKey, configuration));
    }

    @Bean
    public GameStateFactory<NumberState> gameStateFactory() {
        return new NumberStateFactory();
    }

}
