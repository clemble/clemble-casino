package com.clemble.casino.goal.construction.controller;

import com.clemble.casino.bet.Bid;
import com.clemble.casino.bet.PlayerBid;
import com.clemble.casino.goal.lifecycle.initiation.GoalInitiation;
import com.clemble.casino.goal.lifecycle.initiation.service.GoalInitiationService;
import com.clemble.casino.goal.lifecycle.construction.service.ServerGoalInitiationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static com.clemble.casino.goal.GoalWebMapping.GOAL_INITIATION;
import static com.clemble.casino.goal.GoalWebMapping.MY_GOAL_INITIATION;
import static com.clemble.casino.goal.GoalWebMapping.GOAL_INITIATION_BID;
import static com.clemble.casino.WebMapping.PRODUCES;

/**
 * Created by mavarazy on 9/13/14.
 */
@RestController
public class GoalInitiationServiceController implements GoalInitiationService {

    final private ServerGoalInitiationService initiationService;

    public GoalInitiationServiceController(ServerGoalInitiationService initiationService) {
        this.initiationService = initiationService;
    }

    @Override
    public Collection<GoalInitiation> getPending() {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.GET, value = MY_GOAL_INITIATION, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public Collection<GoalInitiation> getPending(@CookieValue("player") String player) {
        return initiationService.getPending(player);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GOAL_INITIATION, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public GoalInitiation get(@PathVariable("goalKey") String goalKey) {
        return initiationService.get(goalKey);
    }

    @Override
    public GoalInitiation bid(String goalKey, Bid bid) {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.POST, value = GOAL_INITIATION_BID, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public GoalInitiation bid(@PathVariable("goalKey") String goalKey, @CookieValue("player") String player, @RequestBody Bid bid) {
        // Step 1. Generating player bid
        PlayerBid playerBid = new PlayerBid(player, bid);
        // Step 2. Processing player bid
        return initiationService.bid(goalKey, playerBid);
    }

}
