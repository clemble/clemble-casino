package com.clemble.casino.server.goal.controller;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.goal.Goal;
import com.clemble.casino.goal.GoalState;
import com.clemble.casino.goal.service.GoalServiceContract;
import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.goal.repository.GoalRepository;
import com.clemble.casino.server.id.IdGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static com.clemble.casino.web.mapping.WebMapping.PRODUCES;
import static com.clemble.casino.goal.GoalWebMapping.*;


/**
 * Created by mavarazy on 8/2/14.
 */
@RestController
public class GoalServiceController implements GoalServiceContract, ExternalController {

    final private GoalRepository goalRepository;
    final private IdGenerator goalIdGenerator;

    public GoalServiceController(IdGenerator idGenerator, GoalRepository goalRepository) {
        this.goalIdGenerator = idGenerator;
        this.goalRepository = goalRepository;
    }


    @RequestMapping(method = RequestMethod.POST, value = MY_GOALS, produces = PRODUCES)
    @ResponseStatus(HttpStatus.CREATED)
    public Goal addMyGoal(@CookieValue("player") String player, @RequestBody Goal goal) {
        // Step 0.1. Checking player is valid
        if(goal.getPlayer() != null && !goal.getPlayer().equals(player))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GoalPlayerIncorrect);
        // Step 0.2. Checking state is pending or null
        if(goal.getState() != null && !GoalState.pending.equals(goal.getState()))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GoalStateIncorrect);
        // Step 0.3. Checking due date
        if(goal.getDueDate() == null || goal.getDueDate().getTime() < System.currentTimeMillis())
            throw ClembleCasinoException.fromError(ClembleCasinoError.GoalDueDateInPast);
        // Step 1. Generating saved goal
        Goal goalToSave = goal.cloneWithPlayerAndGoal(player, goalIdGenerator.newId(), GoalState.pending);
        // Step 2. Saving goal for future
        return goalRepository.save(goalToSave);
    }

    @RequestMapping(method = RequestMethod.GET, value = MY_GOALS, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    public Collection<Goal> myGoals(@CookieValue("player") String player) {
        return goalRepository.findByPlayer(player);
    }

    @RequestMapping(method = RequestMethod.GET, value = MY_GOALS_PENDING, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    public Collection<Goal> myPendingGoals(@CookieValue("player") String player) {
        return goalRepository.findByPlayerAndState(player, GoalState.pending);
    }

    @RequestMapping(method = RequestMethod.GET, value = MY_GOALS_REACHED, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    public Collection<Goal> myReachedGoals(@CookieValue("player") String player) {
        return goalRepository.findByPlayerAndState(player, GoalState.reached);
    }


    @RequestMapping(method = RequestMethod.GET, value = MY_GOALS_MISSED, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    public Collection<Goal> myMissedGoals(@CookieValue("player") String player) {
        return goalRepository.findByPlayerAndState(player, GoalState.missed);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PLAYER_GOALS, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    public Collection<Goal> getGoals(@PathVariable("player") String player) {
        return goalRepository.findByPlayer(player);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PLAYER_GOALS_PENDING, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Collection<Goal> getPendingGoals(@PathVariable("player") String player) {
        return goalRepository.findByPlayerAndState(player, GoalState.pending);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PLAYER_GOALS_REACHED, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Collection<Goal> getReachedGoals(@PathVariable("player") String player) {
        return goalRepository.findByPlayerAndState(player, GoalState.reached);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PLAYER_GOALS_MISSED, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Collection<Goal> getMissedGoals(@PathVariable("player") String player) {
        return goalRepository.findByPlayerAndState(player, GoalState.missed);
    }
}
