package com.gogomaya.server.web.active.session;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.server.game.match.GameMatchingService;
import com.gogomaya.server.game.rule.GameRuleSpecification;
import com.gogomaya.server.game.session.GameSession;

@Controller
public class SessionController {

    final private GameMatchingService matchingService;

    public SessionController(final GameMatchingService matchingService) {
        this.matchingService = checkNotNull(matchingService);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/active/session", produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public GameSession create(@RequestHeader("userId") final long userId, final GameRuleSpecification ruleSpecification) {
        return matchingService.create(userId, ruleSpecification);
    }
}
