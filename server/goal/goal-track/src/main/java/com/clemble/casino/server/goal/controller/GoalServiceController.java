package com.clemble.casino.server.goal.controller;

import com.clemble.casino.bet.Bid;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.goal.*;
import com.clemble.casino.goal.service.GoalService;
import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.event.goal.SystemGoalCreatedEvent;
import com.clemble.casino.server.goal.OldGoalKeyGenerator;
import com.clemble.casino.server.goal.repository.GoalRepository;
import com.clemble.casino.server.goal.service.BidCalculator;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.clemble.casino.web.mapping.WebMapping.PRODUCES;
import static com.clemble.casino.goal.GoalWebMapping.*;


/**
 * Created by mavarazy on 8/2/14.
 */
@RestController
public class GoalServiceController implements GoalService, ExternalController {

    final private OldGoalKeyGenerator goalKeyGenerator;
    final private GoalRepository goalRepository;
    final private BidCalculator bidCalculator;
    final private SystemNotificationService systemNotificationService;

    public GoalServiceController(OldGoalKeyGenerator idGenerator, BidCalculator bidCalculator, GoalRepository goalRepository, SystemNotificationService systemNotificationService) {
        this.goalKeyGenerator = idGenerator;
        this.bidCalculator = bidCalculator;
        this.goalRepository = goalRepository;
        this.systemNotificationService = systemNotificationService;
    }

    @Override
    public Goal addMyGoal(GoalRequest goal) {
        throw new IllegalAccessError();
    }

    @Override
    public GoalStatus myGoalStatuses(String id) {
        throw new IllegalArgumentException();
    }

    @RequestMapping(method = RequestMethod.GET, value = MY_GOALS_GOAL_STATUS, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    public GoalStatus myGoalStatuses(@CookieValue("player") String player, @PathVariable("id") String goalKey) {
        return goalRepository.findOne(goalKey).getStatus();
    }

    @Override
    public GoalStatus updateMyGoal(String id, GoalStatus status) {
        throw new IllegalAccessError();
    }

    @RequestMapping(method = RequestMethod.POST, value = MY_GOALS_GOAL_STATUS, produces = PRODUCES)
    @ResponseStatus(HttpStatus.CREATED)
    public GoalStatus updateMyGoal(@CookieValue("player") String player, @PathVariable("id") String goalKey, @RequestBody GoalStatus status) {
        Goal goal = goalRepository.findOne(goalKey);
        GoalStatus updatedStatus = GoalStatus.create(status.getStatus());
        Goal updatedGoal = goal.cloneWithStatus(updatedStatus);
        goalRepository.save(updatedGoal);
        return updatedStatus;
    }

    @RequestMapping(method = RequestMethod.POST, value = MY_GOALS, produces = PRODUCES)
    @ResponseStatus(HttpStatus.CREATED)
    public Goal addMyGoal(@CookieValue("player") String player, @RequestBody GoalRequest goal) {
        // Step 0. Checking due date
        if (goal.getTimeInDays() <= 0)
            throw ClembleCasinoException.fromError(ClembleCasinoError.GoalDueDateInPast);
        // Step 1. Generating saved goal
        Date startDate = new Date();
        Bid bid = bidCalculator.calculate(goal);
        Goal goalToSave = new Goal(
            goalKeyGenerator.generate(player),
            player,
            goal.getJudge() != null ? goal.getJudge() : player,
            goal.getGoal(),
            startDate,
            new Date(startDate.getTime() + TimeUnit.DAYS.toMillis(goal.getTimeInDays())),
            GoalState.pending,
            new GoalStatus("Go Champ", new Date()),
            bid
        );
        // Step 2. Saving goal for future
        Goal savedGoal = goalRepository.save(goalToSave);
        // Step 3. Sending system notification, for newly created goal
        systemNotificationService.notify(new SystemGoalCreatedEvent(savedGoal));
        // Step 4. We are done returning saved Goal
        return savedGoal;
    }

    @Override
    public Goal myGoal(String id) {
        throw new IllegalAccessError();
    }

    @RequestMapping(method = RequestMethod.GET, value = MY_GOALS_GOAL, produces = PRODUCES)
    @ResponseStatus(HttpStatus.OK)
    public Goal myGoal(@CookieValue("player") String player, @PathVariable("id") String goalKey) {
        return goalRepository.findOne(goalKey);
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
    public Goal getGoal(@PathVariable("player") String player, @PathVariable("id") String goalKey) {
        return goalRepository.findOne(goalKey);
    }

}
