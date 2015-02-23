package com.clemble.casino.goal.suggestion.controller;

import com.clemble.casino.goal.lifecycle.construction.GoalSuggestionResponse;
import com.clemble.casino.goal.lifecycle.construction.event.GoalSuggestionAcceptedEvent;
import com.clemble.casino.goal.lifecycle.construction.event.GoalSuggestionCreatedEvent;
import com.clemble.casino.goal.lifecycle.construction.event.GoalSuggestionDeclinedEvent;
import com.clemble.casino.goal.suggestion.*;
import static com.clemble.casino.goal.GoalWebMapping.*;

import com.clemble.casino.goal.lifecycle.construction.GoalSuggestion;
import com.clemble.casino.goal.lifecycle.construction.GoalSuggestionRequest;
import com.clemble.casino.goal.lifecycle.construction.GoalSuggestionState;
import com.clemble.casino.goal.lifecycle.construction.service.GoalSuggestionService;
import com.clemble.casino.goal.suggestion.repository.GoalSuggestionRepository;
import com.clemble.casino.server.event.goal.SystemGoalInitiationStartedEvent;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.tag.TagUtils;
import org.joda.time.DateTime;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * Created by mavarazy on 1/3/15.
 */
@RestController
public class GoalSuggestionServiceController implements GoalSuggestionService {

    final private GoalSuggestionKeyGenerator keyGenerator;
    final private ServerNotificationService playerNotificationService;
    final private GoalSuggestionRepository suggestionRepository;
    final private SystemNotificationService notificationService;

    public GoalSuggestionServiceController(
        GoalSuggestionKeyGenerator keyGenerator,
        ServerNotificationService playerNotificationService,
        SystemNotificationService notificationService,
        GoalSuggestionRepository suggestionRepository) {
        this.keyGenerator = keyGenerator;
        this.notificationService = notificationService;
        this.suggestionRepository = suggestionRepository;
        this.playerNotificationService = playerNotificationService;
    }

    @Override
    public List<GoalSuggestion> listMy() {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.GET, value = MY_SUGGESTIONS, produces = PRODUCES)
    public List<GoalSuggestion> listMy(@CookieValue("player") String player) {
        return suggestionRepository.findByPlayerAndStateOrderByCreatedDesc(player, GoalSuggestionState.pending);
    }

    @Override
    public List<GoalSuggestion> listMySuggested() {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.GET, value = MY_SUGGESTED, produces = PRODUCES)
    public List<GoalSuggestion> listMySuggested(@CookieValue("player") String player) {
        return suggestionRepository.findBySuggesterOrderByCreatedDesc(player);
    }


    @Override
    @RequestMapping(method = RequestMethod.GET, value = PLAYER_SUGGESTED, produces = PRODUCES)
    public List<GoalSuggestion> listSuggested(@PathVariable("player") String player) {
        return suggestionRepository.findBySuggesterOrderByCreatedDesc(player);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = PLAYER_SUGGESTIONS, produces = PRODUCES)
    public List<GoalSuggestion> list(@PathVariable("player") String player) {
        return suggestionRepository.findByPlayerAndStateOrderByCreatedDesc(player, GoalSuggestionState.pending);
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
            suggestionRequest.getTimezone(),
            TagUtils.getTag(suggestionRequest.getGoal()),
            player,
            suggester,
            GoalSuggestionState.pending,
            DateTime.now());
        // Step 2. Saving and returning new suggestion
        suggestion = suggestionRepository.save(suggestion);
        // Step 3. Sending notification, to user
        playerNotificationService.send(new GoalSuggestionCreatedEvent(player, suggestion));
        // Step 4. Sending suggestion
        return suggestion;
    }

    @Override
    public GoalSuggestion reply(String goalKey, GoalSuggestionResponse accept) {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.POST, value = MY_SUGGESTIONS_GOAL, produces = PRODUCES)
    public GoalSuggestion reply(@CookieValue("player") String player, @PathVariable("goalKey") String goalKey, @RequestBody GoalSuggestionResponse response) {
        // Step 1. Fetching suggestion
        GoalSuggestion suggestion = suggestionRepository.findOne(goalKey);
        if (!suggestion.getPlayer().equals(player) || suggestion.getState() != GoalSuggestionState.pending)
            throw new IllegalAccessError();
        // Step 2. Changing state
        if (response.getAccepted()) {
            suggestion = suggestion.copyWithStatus(GoalSuggestionState.accepted);
            notificationService.send(new SystemGoalInitiationStartedEvent(suggestion.getGoalKey(), suggestion.toInitiation(response.getConfiguration())));
            playerNotificationService.send(new GoalSuggestionAcceptedEvent(player, suggestion));
        } else {
            suggestion = suggestion.copyWithStatus(GoalSuggestionState.declined);
            playerNotificationService.send(new GoalSuggestionDeclinedEvent(player, suggestion));
        }
        // Step 3. Saving new suggestion state
        suggestion = suggestionRepository.save(suggestion);
        // Step 4. Sending notification, to user
        return suggestion;
    }

}
