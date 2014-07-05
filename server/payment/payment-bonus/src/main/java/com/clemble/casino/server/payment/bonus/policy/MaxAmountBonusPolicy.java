package com.clemble.casino.server.payment.bonus.policy;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.payment.money.Money;

public class MaxAmountBonusPolicy implements BonusPolicy {

    final private Money maxAmount;

    public MaxAmountBonusPolicy(Money maxAmount) {
        this.maxAmount = checkNotNull(maxAmount);
    }

    @Override
    public boolean eligible(PaymentTransaction transaction) {
        // TODO restore
        return true;
//        // Step 1. Fetching current amount
//        Money currentAmount = account.getMoney(maxAmount.getCurrency());
//        // Step 2. Checking currentAmount is lesser, then a limmit
//        return currentAmount.getAmount() >= maxAmount.getAmount();
    }

}
