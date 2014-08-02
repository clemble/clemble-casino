package com.clemble.casino.server.goal.web;

import com.clemble.casino.goal.Goal;
import com.clemble.casino.goal.GoalState;
import com.clemble.casino.goal.service.GoalService;
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
public class GoalController implements GoalService {

    final private GoalRepository goalRepository;
    final private IdGenerator goalIdGenerator;

    public GoalController(IdGenerator idGenerator, GoalRepository goalRepository) {
        this.goalIdGenerator = idGenerator;
        this.goalRepository = goalRepository;
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = PLAYER_GOALS, produces = PRODUCES)
    public @ResponseBody Goal addGoal(@PathVariable("player") String player, @RequestBody Goal goal) {
        // Step 1. Generating saved goal
        Goal savedGoal = new Goal(
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
