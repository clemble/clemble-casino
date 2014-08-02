package com.clemble.casino.server.goal.web;

import com.clemble.casino.goal.PlayerGoal;
import com.clemble.casino.goal.PlayerGoalState;
import com.clemble.casino.goal.service.PlayerGoalService;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.server.goal.repository.PlayerGoalRepository;
import com.clemble.casino.server.id.IdGenerator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;

import static com.clemble.casino.web.mapping.WebMapping.PRODUCES;
import static com.clemble.casino.goal.GoalWebMapping.*;


/**
 * Created by mavarazy on 8/2/14.
 */
@Controller
public class PlayerGoalController implements PlayerGoalService {

    final private PlayerGoalRepository goalRepository;
    final private IdGenerator goalIdGenerator;

    public PlayerGoalController(IdGenerator idGenerator, PlayerGoalRepository goalRepository) {
        this.goalIdGenerator = idGenerator;
        this.goalRepository = goalRepository;
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = PLAYER_GOALS, produces = PRODUCES)
    public @ResponseBody PlayerGoal addGoal(@PathVariable("player") String player, @RequestBody PlayerGoal goal) {
        // Step 1. Generating saved goal
        PlayerGoal savedGoal = new PlayerGoal(
            player,
            goalIdGenerator.newId(),
            goal.getDescription(),
            goal.getBet(),
            goal.getDueDate(),
            goal.getRate(),
            goal.getState());
        // Step 2. Saving goal for future
        // TODO add player credentials
        return goalRepository.save(goal);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PLAYER_GOALS, produces = PRODUCES)
    public @ResponseBody Collection<PlayerGoal> getGoals(@PathVariable("player") String player) {
        return goalRepository.findByPlayer(player);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PLAYER_GOALS_PENDING, produces = PRODUCES)
    public @ResponseBody Collection<PlayerGoal> getPendingGoals(@PathVariable("player") String player) {
        return goalRepository.findByPlayerAndState(player, PlayerGoalState.pending);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PLAYER_GOALS_REACHED, produces = PRODUCES)
    public @ResponseBody Collection<PlayerGoal> getReachedGoals(@PathVariable("player") String player) {
        return goalRepository.findByPlayerAndState(player, PlayerGoalState.reached);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PLAYER_GOALS_MISSED, produces = PRODUCES)
    public @ResponseBody Collection<PlayerGoal> getMissedGoals(@PathVariable("player") String player) {
        return goalRepository.findByPlayerAndState(player, PlayerGoalState.missed);
    }
}
