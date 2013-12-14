package com.clemble.casino.server.payment.bonus.policy;

import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PlayerAccount;

public interface BonusPolicy {

    public boolean eligible(PlayerAccount account, PaymentTransaction transaction);

}
