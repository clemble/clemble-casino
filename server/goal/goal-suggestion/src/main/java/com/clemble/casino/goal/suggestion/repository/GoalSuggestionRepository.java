package com.clemble.casino.goal.suggestion.repository;

import com.clemble.casino.goal.lifecycle.construction.GoalSuggestion;
import com.clemble.casino.goal.lifecycle.construction.GoalSuggestionState;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by mavarazy on 1/3/15.
 */
public interface GoalSuggestionRepository extends MongoRepository<GoalSuggestion, String> {

    List<GoalSuggestion> findByPlayer(String player);

    List<GoalSuggestion> findByPlayerAndState(String player, GoalSuggestionState state);

}
