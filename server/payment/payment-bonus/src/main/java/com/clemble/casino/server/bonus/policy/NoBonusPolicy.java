package com.clemble.casino.server.bonus.policy;

import com.clemble.casino.payment.PaymentTransaction;

public class NoBonusPolicy implements BonusPolicy {

    @Override
    public boolean eligible(PaymentTransaction transaction) {
        return true;
    }

}
