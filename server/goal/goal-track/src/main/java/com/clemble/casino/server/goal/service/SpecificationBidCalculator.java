package com.clemble.casino.server.goal.service;

import com.clemble.casino.bet.BetSpecification;
import com.clemble.casino.bet.Bid;
import com.clemble.casino.goal.GoalRequest;

/**
 * Created by mavarazy on 8/16/14.
 */
public class SpecificationBidCalculator implements BidCalculator {

    final private BetSpecification specification;

    public SpecificationBidCalculator(BetSpecification specification) {
        this.specification = specification;
    }

    @Override
    public Bid calculate(GoalRequest request) {
        return specification.toBid(request.getAmount());
    }

}
