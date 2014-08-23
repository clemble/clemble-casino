package com.clemble.casino.goal.controller;

import com.clemble.casino.goal.GoalJudgeDuty;
import com.clemble.casino.goal.repository.GoalJudgeDutyRepository;
import com.clemble.casino.goal.service.*;
import com.clemble.casino.web.mapping.WebMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.clemble.casino.goal.GoalJudgeDutyWebMapping.*;
import static com.clemble.casino.player.PlayerAware.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

/**
 * Created by mavarazy on 8/23/14.
 */
@RestController
public class GoalJudgeDutyServiceController implements GoalJudgeDutyService {

    final GoalJudgeDutyRepository dutyRepository;

    public GoalJudgeDutyServiceController(GoalJudgeDutyRepository dutyRepository) {
        this.dutyRepository = dutyRepository;
    }

    @Override
    public List<GoalJudgeDuty> myDuties() {
        throw new IllegalAccessError();
    }

    @RequestMapping(method = GET, value = MY_DUTIES, produces = WebMapping.PRODUCES)
    @ResponseStatus(OK)
    public List<GoalJudgeDuty> myDuties(@CookieValue(PLAYER) String judge) {
        return dutyRepository.findByJudge(judge);
    }

}
