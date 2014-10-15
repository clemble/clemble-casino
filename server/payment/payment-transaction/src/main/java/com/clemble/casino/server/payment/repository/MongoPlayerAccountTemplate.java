package com.clemble.casino.server.payment.repository;

import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.payment.PendingOperation;
import com.clemble.casino.payment.PlayerAccount;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.Collections;

/**
 * Created by mavarazy on 15/10/14.
 */
public class MongoPlayerAccountTemplate implements PlayerAccountTemplate {

    final private PlayerAccountRepository accountRepository;

    public MongoPlayerAccountTemplate(PlayerAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public PlayerAccount findOne(String player) {
        return accountRepository.findOne(player);
    }

    @Override
    public void debit(String player, Money amount) {
        tryDebit(player, amount);
    }

    private void tryDebit(String player, Money amount) {
        try {
            PlayerAccount account = accountRepository.findOne(player);
            Money debit = account.getMoney(amount.getCurrency());
            if (debit == null) {
                account.getMoney().put(amount.getCurrency(), amount);
            } else {
                account.getMoney().put(amount.getCurrency(), debit.add(amount));
            }
            accountRepository.save(account);
        } catch (OptimisticLockingFailureException e) {
            tryDebit(player, amount);
        } catch (NullPointerException e) {
            // TODO this leaves a control breach for random PaymentAccount creation
            // Step 1. Creating new account
            PlayerAccount newAccount = new PlayerAccount(player, Collections.<Currency, Money>emptyMap(), Collections.<PendingOperation>emptyList(), null);
            // Step 2. Adding new account to repository
            accountRepository.save(newAccount);
        }
    }

    @Override
    public void credit(String player, Money amount) {
        debit(player, amount.negate());
    }

}
