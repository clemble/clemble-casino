package com.clemble.casino.goal.controller;

import com.clemble.casino.WebMapping;
import com.clemble.casino.goal.GoalWebMapping;
import com.clemble.casino.goal.lifecycle.management.GoalVictory;
import com.clemble.casino.goal.lifecycle.management.service.GoalVictoryService;
import com.clemble.casino.goal.repository.GoalVictoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by mavarazy on 3/14/15.
 */
@RestController
public class GoalVictoryServiceController implements GoalVictoryService {

    final private GoalVictoryRepository victoryRepository;

    public GoalVictoryServiceController(GoalVictoryRepository victoryRepository) {
        this.victoryRepository = victoryRepository;
    }

    @Override
    public List<GoalVictory> listMy() {
        throw new IllegalArgumentException();
    }

    @RequestMapping(method = RequestMethod.GET, value = GoalWebMapping.MY_VICTORIES, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public List<GoalVictory> listMy(@CookieValue("player") String me) {
        return victoryRepository.findByPlayer(me);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GoalWebMapping.PLAYER_VICTORIES, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public List<GoalVictory> list(@PathVariable("player") String player) {
        return victoryRepository.findByPlayer(player);
    }

}

