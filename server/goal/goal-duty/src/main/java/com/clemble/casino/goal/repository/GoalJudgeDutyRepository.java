package com.clemble.casino.goal.repository;

import com.clemble.casino.goal.GoalJudgeDuty;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by mavarazy on 8/23/14.
 */
public interface GoalJudgeDutyRepository extends MongoRepository<GoalJudgeDuty, String> {

    public List<GoalJudgeDuty> findByJudge(String judge);

}
