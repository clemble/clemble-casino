package com.gogomaya.server.web.active.session;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.server.game.match.GameMatchingService;
import com.gogomaya.server.game.rule.GameRuleSpecification;
import com.gogomaya.server.game.session.GameSession;
import com.gogomaya.server.game.session.GameSessionRepository;

@Controller
public class SessionController {

    final private GameMatchingService matchingService;
    final private GameSessionRepository sessionRepository;

    public SessionController(final GameMatchingService matchingService, final GameSessionRepository sessionRepository) {
        this.matchingService = checkNotNull(matchingService);
        this.sessionRepository = checkNotNull(sessionRepository);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/active/session", produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody GameSession create(@RequestHeader("playerId") final long userId, final GameRuleSpecification ruleSpecification) {
        return matchingService.create(userId, ruleSpecification);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/active/session/{sessionId}", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody GameSession get(@PathVariable("sessionId") final long sessionId) {
        return sessionRepository.findOne(sessionId);
    }
}
