package com.gogomaya.server.web.game.configuration;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.server.game.configuration.GameConfigurationManager;
import com.gogomaya.server.game.configuration.GameSpecificationOptions;
import com.gogomaya.server.web.mapping.GameWebMapping;

@Controller
public class GameConfigurationManagerController {

    final GameConfigurationManager configurationManager;

    public GameConfigurationManagerController(GameConfigurationManager configurationManager) {
        this.configurationManager = checkNotNull(configurationManager);
    }

    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.GAME_OPTIONS, produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    // Can be null
    public @ResponseBody
    GameSpecificationOptions get(@RequestHeader(value = "playerId", required = false) final Long playerId) {
        return configurationManager.getSpecificationOptions();
    }

}
