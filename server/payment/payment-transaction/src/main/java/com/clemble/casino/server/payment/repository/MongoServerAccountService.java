package com.clemble.casino.server.payment.repository;

import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.payment.*;
import com.clemble.casino.payment.event.PaymentCompleteEvent;
import com.clemble.casino.payment.event.PaymentFreezeEvent;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by mavarazy on 15/10/14.
 */
public class MongoServerAccountService implements ServerAccountService {

    final private Logger LOG = LoggerFactory.getLogger(ServerAccountService.class);

    final private PlayerAccountRepository accountRepository;
    final private PaymentTransactionRepository transactionRepository;
    final private PendingTransactionRepository pendingTransactionRepository;
    final private ServerNotificationService notificationService;

    public MongoServerAccountService(
        PlayerAccountRepository accountRepository,
        PaymentTransactionRepository transactionRepository,
        PendingTransactionRepository pendingTransactionRepository,
        ServerNotificationService notificationService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.notificationService = notificationService;
        this.pendingTransactionRepository = pendingTransactionRepository;
    }

    @Override
    public PlayerAccount findOne(String player) {
        return accountRepository.findOne(player);
    }

    @Override
    public PaymentTransaction process(PaymentTransaction transaction) {
        return tryProcess(transaction);
    }

    private PaymentTransaction tryProcess(PaymentTransaction transaction) {
        // Step 0. Sanity check
        if(transactionRepository.exists(transaction.getTransactionKey())) {
            // TODO maybe you need to throw exception here
            LOG.error("Payment transaction already exists {}", transaction);
            return transactionRepository.findOne(transaction.getTransactionKey());
        }
        try {
            final PaymentTransaction savedTransaction = transactionRepository.save(transaction);
            // Step 1. Processing payment operation one at a time
            PendingTransaction pendingTransaction = pendingTransactionRepository.findOne(transaction.getTransactionKey());
            // Step 2. Updating all related accounts
            for(PaymentOperation operation : savedTransaction.getOperations()) {
                // Step 2.1. Sending notification for processed payment
                PlayerAccount account = (pendingTransaction != null && pendingTransaction.getOperation(operation.getPlayer(), operation.getAmount().getCurrency()) != null)
                    ? tryProcess(operation.combine(pendingTransaction.getOperation(operation.getPlayer(), operation.getAmount().getCurrency()).toOpposite()))
                    : tryProcess(operation);
                // Step 2.1. Sending notification for processed payment
                notificationService.send(new PaymentCompleteEvent(transaction.getTransactionKey(), operation, transaction.getSource(), account));
            }
            // Step 3. Removing pending transaction
            pendingTransactionRepository.delete(transaction.getTransactionKey());
            return savedTransaction;
        } catch (OptimisticLockingFailureException e) {
            // TODO This is dangerous approach to this problem
            return tryProcess(transaction);
        }
    }

    private PlayerAccount tryProcess(PaymentOperation operation) {
        try {
            PlayerAccount account = accountRepository.findOne(operation.getPlayer());
            account.process(operation);
            return accountRepository.save(account);
        } catch (OptimisticLockingFailureException e) {
            // TODO This is dangerous approach to this problem
            return tryProcess(operation);
        } catch (NullPointerException e) {
            tryCreate(operation.getPlayer());
            // TODO this leaves a control breach for random PaymentAccount creation
            return tryProcess(operation);
        }
    }

    private void tryCreate(String player) {
        try {
            // TODO this leaves a control breach for random PaymentAccount creation
            // Step 1. Creating new account
            Map<Currency, Money> moneyMap = new EnumMap<Currency, Money>(Currency.class);
            for (Currency currency: Currency.values())
                moneyMap.put(currency, Money.create(currency, 0));
            PlayerAccount newAccount = new PlayerAccount(player, moneyMap, null);
            // Step 2. Adding new account to repository
            accountRepository.save(newAccount);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public PendingTransaction freeze(PendingTransaction transaction) {
        // All payment freeze events accumulated in a collection
        transaction.getOperations().forEach(operation -> {
            // Step 1. Adding to pending transactions
            tryAddingToPending(transaction.getTransactionKey(), operation);
            // Step 2. Processing transaction
            tryProcess(operation);
            // Step 3. Send freeze notification
            PaymentFreezeEvent freezeEvent = new PaymentFreezeEvent(operation.getPlayer(), transaction.getTransactionKey(), operation.getAmount());
            notificationService.send(freezeEvent);
        });
        // Step 5. Returning created transaction
        return pendingTransactionRepository.findOne(transaction.getTransactionKey());
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
