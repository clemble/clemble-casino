package com.clemble.casino.server.goal.service;

import com.clemble.casino.bet.Bid;
import com.clemble.casino.goal.GoalRequest;

/**
 * Created by mavarazy on 8/16/14.
 */
public interface BidCalculator {

    public Bid calculate(GoalRequest request);

}
