package com.clemble.casino.server.goal.controller;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.goal.Goal;
import com.clemble.casino.goal.GoalKey;
import com.clemble.casino.goal.GoalState;
import com.clemble.casino.goal.GoalStatus;
import com.clemble.casino.goal.service.GoalService;
import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.goal.repository.GoalRepository;
import com.clemble.casino.server.id.IdGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;

import static com.clemble.casino.web.mapping.WebMapping.PRODUCES;
import static com.clemble.casino.goal.GoalWebMapping.*;


/**
 * Created by mavarazy on 8/2/14.
 */
@RestController
public class GoalServiceController implements GoalService, ExternalController {

    final private GoalRepository goalRepository;
    final private IdGenerator goalIdGenerator;

    public GoalServiceController(IdGenerator idGenerator, GoalRepository goalRepository) {
        this.goalIdGenerator = idGenerator;
        this.goalRepository = goalRepository;
    }

    @Override
    public Goal addMyGoal(Goal goal) {
        throw new IllegalAccessError();
    }

    @Override
    public GoalStatus myGoalStatuses(String id) {
        throw new IllegalArgumentException();
    }

    @RequestMapping(method = RequestMethod.GET, value = MY_GOALS_GOAL_STATUS, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    public GoalStatus myGoalStatuses(@CookieValue("player") String player, @PathVariable("id") String id) {
        return goalRepository.findOne(new GoalKey(player, id)).getStatus();
    }

    @Override
    public GoalStatus updateMyGoal(String id, GoalStatus status) {
        throw new IllegalAccessError();
    }

    @RequestMapping(method = RequestMethod.POST, value = MY_GOALS_GOAL_STATUS, produces = PRODUCES)
    @ResponseStatus(HttpStatus.CREATED)
    public GoalStatus updateMyGoal(@CookieValue("player") String player, @PathVariable("id") String id, @RequestBody GoalStatus status) {
        Goal goal = goalRepository.findOne(new GoalKey(player, id));
        GoalStatus updatedStatus = GoalStatus.create(status.getStatus());
        Goal updatedGoal = goal.cloneWithStatus(updatedStatus);
        goalRepository.save(updatedGoal);
        return updatedStatus;
    }

    @RequestMapping(method = RequestMethod.POST, value = MY_GOALS, produces = PRODUCES)
    @ResponseStatus(HttpStatus.CREATED)
    public Goal addMyGoal(@CookieValue("player") String player, @RequestBody Goal goal) {
        // Step 0.1. Checking player is valid
        if (goal.getGoalKey() != null && goal.getGoalKey().getPlayer() != null && !goal.getGoalKey().getPlayer().equals(player))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GoalPlayerIncorrect);
        // Step 0.2. Checking state is pending or null
        if (goal.getState() != null && !GoalState.pending.equals(goal.getState()))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GoalStateIncorrect);
        // Step 0.3. Checking due date
        if (goal.getDueDate() == null || goal.getDueDate().getTime() < System.currentTimeMillis())
            throw ClembleCasinoException.fromError(ClembleCasinoError.GoalDueDateInPast);
//        if (goal.getBid() == null || goal.getBid().getAmount().getAmount() < 0 || !player.equals(goal.getBid().getBidder()))
//            throw ClembleCasinoException.fromError(ClembleCasinoError.GoalBidInvalid);
        // Step 1. Generating saved goal
        Goal goalToSave = new Goal(
            new GoalKey(player, goalIdGenerator.newId()),
            player,
            goal.getDescription(),
            new Date(),
            goal.getDueDate(),
            GoalState.pending,
            new GoalStatus("Go", new Date())
        );
        // Step 2. Saving goal for future
        return goalRepository.save(goalToSave);
    }

    @Override
    public Goal myGoal(String id) {
        throw new IllegalAccessError();
    }

    @RequestMapping(method = RequestMethod.GET, value = MY_GOALS_GOAL, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    public Goal myGoal(@CookieValue("player") String player, @PathVariable("id") String id) {
        return goalRepository.findOne(new GoalKey(player, id));
    }

    @Override
    public Collection<Goal> myGoals() {
        throw new IllegalAccessError();
    }

    @RequestMapping(method = RequestMethod.GET, value = MY_GOALS, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    public Collection<Goal> myGoals(@CookieValue("player") String player) {
        return goalRepository.findByPlayer(player);
    }

    @Override
    public Collection<Goal> myPendingGoals() {
        throw new IllegalAccessError();
    }

    @RequestMapping(method = RequestMethod.GET, value = MY_GOALS_PENDING, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    public Collection<Goal> myPendingGoals(@CookieValue("player") String player) {
        return goalRepository.findByPlayerAndState(player, GoalState.pending);
    }

    @Override
    public Collection<Goal> myReachedGoals() {
        throw new IllegalAccessError();
    }

    @RequestMapping(method = RequestMethod.GET, value = MY_GOALS_REACHED, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    public Collection<Goal> myReachedGoals(@CookieValue("player") String player) {
        return goalRepository.findByPlayerAndState(player, GoalState.reached);
    }

    @Override
    public Collection<Goal> myMissedGoals() {
        throw new IllegalAccessError();
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

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PLAYER_GOALS_GOAL, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    public Goal getGoal(@PathVariable("player") String player, @PathVariable("id") String id) {
        return goalRepository.findOne(new GoalKey(player, id));
    }

}
