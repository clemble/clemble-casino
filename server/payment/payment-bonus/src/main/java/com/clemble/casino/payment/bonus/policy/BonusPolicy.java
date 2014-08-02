package com.clemble.casino.payment.bonus.policy;

import com.clemble.casino.payment.PaymentTransaction;

public interface BonusPolicy {

    public boolean eligible(PaymentTransaction transaction);

}
