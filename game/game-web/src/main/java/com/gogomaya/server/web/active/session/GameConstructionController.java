package com.gogomaya.server.web.active.session;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.GameTable;
import com.gogomaya.server.game.build.GameConstructionService;
import com.gogomaya.server.game.configuration.GameConfigurationManager;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.table.GameTableRepository;

@Controller
public class GameConstructionController<State extends GameState> {

    final private GameConfigurationManager configurationManager;

    final private GameConstructionService<State> constructionService;

    final private GameTableRepository<State> tableRepository;

    public GameConstructionController(final GameConstructionService<State> matchingService,
            final GameTableRepository<State> sessionRepository,
            final GameConfigurationManager configurationManager) {
        this.constructionService = checkNotNull(matchingService);
        this.tableRepository = checkNotNull(sessionRepository);
        this.configurationManager = checkNotNull(configurationManager);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/active/session", produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody GameTable<State> match(@RequestHeader("playerId") final long playerId, 
            @RequestBody final GameSpecification gameSpecification) {
        // Step 1. Checking that provided specification was valid
        if (!configurationManager.getSpecificationOptions().valid(gameSpecification))
            throw GogomayaException.create(GogomayaError.GameSpecificationInvalid);
        // Step 2. Invoking actual matching service
        return constructionService.instantGame(playerId, gameSpecification);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/active/session/{sessionId}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody GameTable<State> get(@PathVariable("tableId") final long tableId) {
        return tableRepository.findOne(tableId);
    }
}
