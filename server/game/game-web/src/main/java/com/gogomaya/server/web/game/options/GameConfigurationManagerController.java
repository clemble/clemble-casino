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

import com.gogomaya.game.Game;
import com.gogomaya.game.configuration.GameSpecificationOptions;
import com.gogomaya.game.service.GameSpecificationService;
import com.gogomaya.server.game.configuration.GameSpecificationRegistry;
import com.gogomaya.web.game.GameWebMapping;
import com.gogomaya.web.mapping.WebMapping;

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
    public @ResponseBody GameSpecificationOptions getSpecificationOptions(@RequestHeader(value = "playerId", required = false) final long playerId, @PathVariable("name") Game game) {
        return configurationManager.getSpecificationOptions(game);
    }

}
