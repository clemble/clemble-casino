package com.gogomaya.server.web.game.options;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.server.game.Game;
import com.gogomaya.server.game.configuration.GameSpecificationOptions;
import com.gogomaya.server.game.configuration.GameSpecificationRegistry;
import com.gogomaya.server.web.mapping.GameWebMapping;

@Controller
public class GameConfigurationManagerController {

    final private GameSpecificationRegistry configurationManager;

    public GameConfigurationManagerController(GameSpecificationRegistry configurationManager) {
        this.configurationManager = checkNotNull(configurationManager);
    }

    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.GAME_SPECIFICATION_OPTIONS, produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    // Can be null
    public @ResponseBody GameSpecificationOptions get(@RequestHeader(value = "playerId", required = false) final Long playerId, @PathVariable("name") Game game) {
        return configurationManager.getSpecificationOptions(game);
    }

}
