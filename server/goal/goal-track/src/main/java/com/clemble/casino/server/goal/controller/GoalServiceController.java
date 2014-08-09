package com.clemble.casino.server.goal.controller;

import com.clemble.casino.goal.Goal;
import com.clemble.casino.goal.GoalState;
import com.clemble.casino.goal.service.GoalServiceContract;
import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.goal.repository.GoalRepository;
import com.clemble.casino.server.id.IdGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static com.clemble.casino.web.mapping.WebMapping.PRODUCES;
import static com.clemble.casino.goal.GoalWebMapping.*;


/**
 * Created by mavarazy on 8/2/14.
 */
@Controller
public class GoalServiceController implements GoalServiceContract, ExternalController {

    final private GoalRepository goalRepository;
    final private IdGenerator goalIdGenerator;

    public GoalServiceController(IdGenerator idGenerator, GoalRepository goalRepository) {
        this.goalIdGenerator = idGenerator;
        this.goalRepository = goalRepository;
    }


    @RequestMapping(method = RequestMethod.POST, value = MY_GOALS, produces = PRODUCES)
    public @ResponseBody Goal addMyGoal(@CookieValue("player") String player, @RequestBody Goal goal) {
        // Step 1. Generating saved goal
        Goal goalToSave = goal.cloneWithPlayerAndGoal(player,goalIdGenerator.newId());
        // Step 2. Saving goal for future
        // TODO add player credentials
        return goalRepository.save(goalToSave);
    }

    @RequestMapping(method = RequestMethod.GET, value = MY_GOALS, produces = PRODUCES)
    public @ResponseBody Collection<Goal> myGoals(@CookieValue("player") String player) {
        return goalRepository.findByPlayer(player);
    }

    @RequestMapping(method = RequestMethod.GET, value = MY_GOALS_PENDING, produces = PRODUCES)
    public @ResponseBody Collection<Goal> myPendingGoals(@CookieValue("player") String player) {
        return goalRepository.findByPlayerAndState(player, GoalState.pending);
    }

    @RequestMapping(method = RequestMethod.GET, value = MY_GOALS_REACHED, produces = PRODUCES)
    public @ResponseBody Collection<Goal> myReachedGoals(@CookieValue("player") String player) {
        return goalRepository.findByPlayerAndState(player, GoalState.reached);
    }


    @RequestMapping(method = RequestMethod.GET, value = MY_GOALS_MISSED, produces = PRODUCES)
    public @ResponseBody Collection<Goal> myMissedGoals(@CookieValue("player") String player) {
        return goalRepository.findByPlayerAndState(player, GoalState.missed);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PLAYER_GOALS, produces = PRODUCES)
    public @ResponseBody Collection<Goal> getGoals(@PathVariable("player") String player) {
        return goalRepository.findByPlayer(player);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PLAYER_GOALS_PENDING, produces = PRODUCES)
    public @ResponseBody Collection<Goal> getPendingGoals(@PathVariable("player") String player) {
        return goalRepository.findByPlayerAndState(player, GoalState.pending);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PLAYER_GOALS_REACHED, produces = PRODUCES)
    public @ResponseBody Collection<Goal> getReachedGoals(@PathVariable("player") String player) {
        return goalRepository.findByPlayerAndState(player, GoalState.reached);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PLAYER_GOALS_MISSED, produces = PRODUCES)
    public @ResponseBody Collection<Goal> getMissedGoals(@PathVariable("player") String player) {
        return goalRepository.findByPlayerAndState(player, GoalState.missed);
    }
}
