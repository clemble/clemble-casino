package com.clemble.casino.server.web.game.options;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.clemble.casino.game.service.GameConfigurationService;
import com.clemble.casino.game.specification.GameConfigurations;
import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.game.configuration.ServerGameConfigurationService;
import com.clemble.casino.web.game.GameWebMapping;
import com.clemble.casino.web.mapping.WebMapping;

@Controller
public class GameConfigurationController implements GameConfigurationService, ExternalController {

    final private ServerGameConfigurationService configurationService;

    public GameConfigurationController(ServerGameConfigurationService configurationService) {
        this.configurationService = checkNotNull(configurationService);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.GAME_CONFIGURATIONS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody GameConfigurations getConfigurations() {
        return configurationService.getConfigurations();
    }

}
