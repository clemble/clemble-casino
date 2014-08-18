package com.clemble.casino.goal.repository;

import com.clemble.casino.goal.GoalJudgeInvitation;
import com.clemble.casino.goal.GoalKey;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by mavarazy on 8/17/14.
 */
public interface GoalJudgeInvitationRepository extends MongoRepository<GoalJudgeInvitation, GoalKey> {

    public List<GoalJudgeInvitation> findByPlayer(String player);

    public List<GoalJudgeInvitation> findByJudge(String player);

    public List<GoalJudgeInvitation> findByPlayerOrJudge(String player);

}
