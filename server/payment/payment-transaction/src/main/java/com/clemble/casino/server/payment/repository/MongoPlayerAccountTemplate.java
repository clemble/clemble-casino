package com.clemble.casino.server.payment.repository;

import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PendingOperation;
import com.clemble.casino.payment.PendingTransaction;
import com.clemble.casino.payment.PlayerAccount;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.Collections;

/**
 * Created by mavarazy on 15/10/14.
 */
public class MongoPlayerAccountTemplate implements PlayerAccountTemplate {

    final private PlayerAccountRepository accountRepository;
    final private PendingTransactionRepository pendingTransactionRepository;

    public MongoPlayerAccountTemplate(
        PlayerAccountRepository accountRepository,
        PendingTransactionRepository pendingTransactionRepository) {
        this.accountRepository = accountRepository;
        this.pendingTransactionRepository = pendingTransactionRepository;
    }

    @Override
    public PlayerAccount findOne(String player) {
        return accountRepository.findOne(player);
    }

    @Override
    public PlayerAccount process(String transactionKey, PaymentOperation operation) {
        return tryProcess(transactionKey, operation);
    }

    private PlayerAccount tryProcess(String transactionKey, PaymentOperation operation) {
        try {
            String player = operation.getPlayer();
            PlayerAccount account = accountRepository.findOne(player);
            // Step 1. Fetching pendingOperation
            // TODO Check case multiple bets for the same operation
            PendingOperation pendingOperation = null;
            for(PendingOperation pOperation: account.getPendingOperations()) {
                if (transactionKey.equals(pOperation.getTransactionKey()))
                    pendingOperation = pOperation;
            }
            account.getPendingOperations().remove(pendingOperation);
            // Step 2. Performing actual debit operation
            Money amount = operation.toDebit().getAmount();
            Money debit = account.getMoney(amount.getCurrency());
            account.getMoney().put(amount.getCurrency(), amount.add(debit));
            return accountRepository.save(account);
        } catch (OptimisticLockingFailureException e) {
            // TODO This is dangerous approach to this problem
            return tryProcess(transactionKey, operation);
        } catch (NullPointerException e) {
            // TODO this leaves a control breach for random PaymentAccount creation
            tryCreate(operation.getPlayer());
            return tryProcess(transactionKey, operation);
        }
    }

    private void tryCreate(String player) {
        try {
            // TODO this leaves a control breach for random PaymentAccount creation
            // Step 1. Creating new account
            PlayerAccount newAccount = new PlayerAccount(player, Collections.<Currency, Money>emptyMap(), Collections.<PendingOperation>emptyList(), null);
            // Step 2. Adding new account to repository
            accountRepository.save(newAccount);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public PendingTransaction freeze(PendingTransaction pendingTransaction) {
        for (PaymentOperation operation: pendingTransaction.getOperations()) {
            // Step 1. Changing PlayerAccount
            tryFreezing(pendingTransaction.getTransactionKey(), operation);
            // Step 2. Adding to PendingTransactions list
            tryAddingToPending(pendingTransaction.getTransactionKey(), operation);
        }
        return pendingTransactionRepository.findOne(pendingTransaction.getTransactionKey());
    }

    private PlayerAccount tryFreezing(String transactionKey, PaymentOperation operation) {
        try {
            String player = operation.getPlayer();
            PlayerAccount account = accountRepository.findOne(player);
            // Step 1. Fetching pendingOperation
            // TODO Check case multiple bets for the same operation
            account.getPendingOperations().add(PendingOperation.fromOperation(transactionKey, operation));
            // Step 2. Performing actual debit operation
            Money amount = operation.toDebit().getAmount();
            Money debit = account.getMoney(amount.getCurrency());
            account.getMoney().put(amount.getCurrency(), amount.add(debit));
            return accountRepository.save(account);
        } catch (OptimisticLockingFailureException e) {
            // TODO This is dangerous approach to this problem
            return tryFreezing(transactionKey, operation);
        } catch (NullPointerException e) {
            // TODO this leaves a control breach for random PaymentAccount creation
            tryCreate(operation.getPlayer());
            return tryFreezing(transactionKey, operation);
        }
    }

    private PendingTransaction tryAddingToPending(String transactionKey, PaymentOperation operation) {
        try {
            // Step 1. Creating or reading PendingTransaction
            PendingTransaction transaction = pendingTransactionRepository.findOne(transactionKey);
            if (transaction == null) {
                pendingTransactionRepository.save(new PendingTransaction(transactionKey, Collections.emptySet(), null));
                transaction = pendingTransactionRepository.findOne(transactionKey);
            }
            // Step 2. Updating PendingTransaction
            transaction.getOperations().add(operation);
            // Step 3. Saving updated PendingTransaction
            return pendingTransactionRepository.save(transaction);
        } catch (OptimisticLockingFailureException e) {
            // TODO This is dangerous approach to this problem
            return tryAddingToPending(transactionKey, operation);
        } catch (NullPointerException e) {
            return tryAddingToPending(transactionKey, operation);
        }
    }

}
