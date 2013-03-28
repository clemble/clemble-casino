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

import com.gogomaya.server.game.GameSpecification;
import com.gogomaya.server.game.match.GameStateManager;
import com.gogomaya.server.game.session.GameSession;
import com.gogomaya.server.game.session.GameSessionRepository;
import com.gogomaya.server.game.table.GameTable;

@Controller
public class SessionController {

    final private GameStateManager matchingService;
    final private GameSessionRepository sessionRepository;

    public SessionController(final GameStateManager matchingService, final GameSessionRepository sessionRepository) {
        this.matchingService = checkNotNull(matchingService);
        this.sessionRepository = checkNotNull(sessionRepository);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/active/session", produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody GameTable create(@RequestHeader("playerId") final long playerId, @RequestBody final GameSpecification gameSpecification) {
        return matchingService.reserve(playerId, gameSpecification);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/active/session/{sessionId}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody GameSession get(@PathVariable("sessionId") final long sessionId) {
        return sessionRepository.findOne(sessionId);
    }
}
