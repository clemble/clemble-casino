package com.clemble.casino.server.bonus.policy;

import com.clemble.casino.payment.PaymentTransaction;

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
            public boolean eligible(PaymentTransaction transaction) {
                return !bonusPolicy.eligible(transaction);
            }
        });
    }

    public BonusPolicies and(final BonusPolicy bonusPolicy) {
        return new BonusPolicies(new BonusPolicy() {
            @Override
            public boolean eligible(PaymentTransaction transaction) {
                return delegate.eligible(transaction) && bonusPolicy.eligible(transaction);
            }
        });
    }

    public BonusPolicies or(final BonusPolicy bonusPolicy) {
        return new BonusPolicies(new BonusPolicy() {
            @Override
            public boolean eligible(PaymentTransaction transaction) {
                return delegate.eligible(transaction) || bonusPolicy.eligible(transaction);
            }
        });
    }

    @Override
    public boolean eligible(PaymentTransaction transaction) {
        return delegate.eligible(transaction);
    }

}
