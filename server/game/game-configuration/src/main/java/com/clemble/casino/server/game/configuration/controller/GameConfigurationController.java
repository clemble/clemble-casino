package com.clemble.casino.server.game.configuration.controller;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.game.configuration.GameConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.clemble.casino.game.service.GameConfigurationService;
import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.game.configuration.service.ServerGameConfigurationService;

import java.util.List;

import static com.clemble.casino.game.GameWebMapping.*;

@RestController
public class GameConfigurationController implements GameConfigurationService, ExternalController {

    final private static GameConfiguration[] CONFIGURATIONS_ARRAY_MARKER = new GameConfiguration[0];

    final private ServerGameConfigurationService configurationService;

    public GameConfigurationController(ServerGameConfigurationService configurationService) {
        this.configurationService = checkNotNull(configurationService);
    }

    @Override
    public List<GameConfiguration> getConfigurations() {
        return configurationService.getConfigurations();
    }

    // TODO find a better solution, this is workaround for List serialization defisency in Jackson
    @RequestMapping(method = RequestMethod.GET, value = CONFIGURATION, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public GameConfiguration[] getConfigurationsArray() {
        return configurationService.getConfigurations().toArray(CONFIGURATIONS_ARRAY_MARKER);
    }

}
