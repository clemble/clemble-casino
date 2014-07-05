package com.clemble.casino.server.payment.bonus.policy;

import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PlayerAccount;

public class NoBonusPolicy implements BonusPolicy {

    @Override
    public boolean eligible(PaymentTransaction transaction) {
        return true;
    }

}
