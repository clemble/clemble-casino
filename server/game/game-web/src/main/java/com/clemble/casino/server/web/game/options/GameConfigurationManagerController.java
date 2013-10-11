package com.clemble.casino.server.web.game.options;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.configuration.GameSpecificationOptions;
import com.clemble.casino.game.service.GameSpecificationService;
import com.clemble.casino.server.game.configuration.GameSpecificationRegistry;
import com.clemble.casino.web.mapping.WebMapping;
import com.clemble.casino.web.game.GameWebMapping;

@Controller
public class GameConfigurationManagerController implements GameSpecificationService {

    final private GameSpecificationRegistry configurationManager;

    public GameConfigurationManagerController(GameSpecificationRegistry configurationManager) {
        this.configurationManager = checkNotNull(configurationManager);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.GAME_SPECIFICATION_OPTIONS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    // Can be null
    public @ResponseBody GameSpecificationOptions getSpecificationOptions(@RequestHeader(value = "playerId", required = false) final String player, @PathVariable("name") Game game) {
        return configurationManager.getSpecificationOptions(game);
    }

}
