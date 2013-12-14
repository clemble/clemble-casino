package com.clemble.casino.server.payment.bonus.policy;

import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PlayerAccount;

public class BonusPolicies implements BonusPolicy {

    final private BonusPolicy delegate;

    private BonusPolicies(BonusPolicy bonusPolicy) {
        this.delegate = bonusPolicy;
    }

    public static BonusPolicy where(final BonusPolicy bonusPolicy) {
        return new BonusPolicies(bonusPolicy);
    }

    public static BonusPolicies not(final BonusPolicy bonusPolicy) {
        return new BonusPolicies(new BonusPolicy() {
            @Override
            public boolean eligible(PlayerAccount account, PaymentTransaction transaction) {
                return !bonusPolicy.eligible(account, transaction);
            }
        });
    }

    public BonusPolicies and(final BonusPolicy bonusPolicy) {
        return new BonusPolicies(new BonusPolicy() {
            @Override
            public boolean eligible(PlayerAccount account, PaymentTransaction transaction) {
                return delegate.eligible(account, transaction) && bonusPolicy.eligible(account, transaction);
            }
        });
    }

    public BonusPolicies or(final BonusPolicy bonusPolicy) {
        return new BonusPolicies(new BonusPolicy() {
            @Override
            public boolean eligible(PlayerAccount account, PaymentTransaction transaction) {
                return delegate.eligible(account, transaction) || bonusPolicy.eligible(account, transaction);
            }
        });
    }

    @Override
    public boolean eligible(PlayerAccount account, PaymentTransaction transaction) {
        return delegate.eligible(account, transaction);
    }

}
