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

@Controller
public class GameConfiguartionManagerController {

    final GameConfigurationManager configurationManager;
    
    public GameConfiguartionManagerController(GameConfigurationManager configurationManager) {
        this.configurationManager = checkNotNull(configurationManager);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/active/options", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody GameSpecificationOptions create(@RequestHeader(value = "playerId", required = false) final long playerId) {
        return configurationManager.getSpecificationOptions();
    }

}
