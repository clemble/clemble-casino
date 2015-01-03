package com.clemble.casino.goal.suggestion.controller;

import com.clemble.casino.goal.suggestion.*;
import static com.clemble.casino.goal.GoalWebMapping.*;

import com.clemble.casino.goal.lifecycle.construction.GoalSuggestion;
import com.clemble.casino.goal.lifecycle.construction.GoalSuggestionRequest;
import com.clemble.casino.goal.lifecycle.construction.GoalSuggestionState;
import com.clemble.casino.goal.lifecycle.construction.service.GoalSuggestionService;
import com.clemble.casino.goal.suggestion.repository.GoalSuggestionRepository;
import com.clemble.casino.server.event.goal.SystemGoalInitiationDueEvent;
import com.clemble.casino.server.event.goal.SystemGoalInitiationStartedEvent;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * Created by mavarazy on 1/3/15.
 */
@RestController
public class GoalSuggestionServiceController implements GoalSuggestionService {

    final private GoalSuggestionKeyGenerator keyGenerator;
    final private GoalSuggestionRepository suggestionRepository;
    final private SystemNotificationService notificationService;

    public GoalSuggestionServiceController(GoalSuggestionKeyGenerator keyGenerator, SystemNotificationService notificationService, GoalSuggestionRepository suggestionRepository) {
        this.keyGenerator = keyGenerator;
        this.notificationService = notificationService;
        this.suggestionRepository = suggestionRepository;
    }

    @Override
    public List<GoalSuggestion> listMy() {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.GET, value = MY_SUGGESTIONS, produces = PRODUCES)
    public List<GoalSuggestion> listMy(@CookieValue("player") String player) {
        return suggestionRepository.findByPlayerAndState(player, GoalSuggestionState.pending);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PLAYER_SUGGESTIONS, produces = PRODUCES)
    public List<GoalSuggestion> list(@PathVariable("player") String player) {
        return suggestionRepository.findByPlayerAndState(player, GoalSuggestionState.pending);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = SUGGESTION, produces = PRODUCES)
    public GoalSuggestion getSuggestion(@PathVariable("goalKey") String goalKey) {
        return suggestionRepository.findOne(goalKey);
    }

    @Override
    public GoalSuggestion addSuggestion(String player, GoalSuggestionRequest suggestionRequest) {
        throw new UnsupportedOperationException();
    }


    @RequestMapping(method = RequestMethod.POST, value = PLAYER_SUGGESTIONS, produces = PRODUCES)
    public GoalSuggestion addSuggestion(@CookieValue("player") String suggester, @PathVariable("player") String player, @RequestBody GoalSuggestionRequest suggestionRequest) {
        // Step 1. Creating new suggestion
        GoalSuggestion suggestion = new GoalSuggestion(
            keyGenerator.generate(player),
            suggestionRequest.getGoal(),
            player,
            suggester,
            suggestionRequest.getConfiguration(),
            GoalSuggestionState.pending);
        // Step 2. Saving and returning new suggestion
        return suggestionRepository.save(suggestion);
    }

    @Override
    public GoalSuggestion reply(String goalKey, boolean accept) {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.POST, value = MY_SUGGESTIONS_GOAL, produces = PRODUCES)
    public GoalSuggestion reply(@CookieValue("player") String player, @PathVariable("goalKey") String goalKey, @RequestBody boolean accept) {
        // Step 1. Fetching suggestion
        GoalSuggestion suggestion = suggestionRepository.findOne(goalKey);
        if (!suggestion.getPlayer().equals(player) || suggestion.getState() != GoalSuggestionState.pending)
            throw new IllegalAccessError();
        // Step 2. Changing state
        if (accept) {
            suggestion = suggestion.copyWithStatus(GoalSuggestionState.accepted);
            //
            notificationService.send(new SystemGoalInitiationStartedEvent(suggestion.getGoalKey(), suggestion.toInitiation()));
        } else {
            suggestion = suggestion.copyWithStatus(GoalSuggestionState.declined);
        }
        // Step 3. Saving new suggestion state
        suggestion = suggestionRepository.save(suggestion);
        // Step 4. Sending notification, to start new goal

        return suggestion;
    }
}
