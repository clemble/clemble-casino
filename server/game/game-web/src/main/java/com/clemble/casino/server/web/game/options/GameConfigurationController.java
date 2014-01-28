package com.clemble.casino.server.web.game.options;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.clemble.casino.game.service.GameConfigurationService;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.game.specification.PotGameConfiguration;
import com.clemble.casino.game.specification.TournamentGameConfiguration;
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
    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.GAME_SPECIFICATION_OPTIONS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public List<MatchGameConfiguration> getMatchConfigurations() {
        return configurationService.getMatchConfigurations();
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.GAME_POT_SPECIFICATION_OPTIONS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public List<PotGameConfiguration> getPotConfigurations() {
        return configurationService.getPotConfigurations();
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.GAME_TOURNAMENT_SPECIFICATION_OPTIONS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public List<TournamentGameConfiguration> getTournamentConfigurations() {
        return configurationService.getTournamentConfigurations();
    }

}
