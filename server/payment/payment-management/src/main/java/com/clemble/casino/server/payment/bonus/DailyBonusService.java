package com.clemble.casino.server.payment.bonus;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.clemble.casino.money.MoneySource;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.payment.PaymentTransactionKey;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.payment.money.Operation;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.payment.PaymentTransactionServerService;
import com.clemble.casino.server.repository.payment.PaymentTransactionRepository;

public class DailyBonusService implements BonusService {

    final private DateFormat DATE_FORMAT = new SimpleDateFormat("dd/mm/yy");

    final private Money bonusAmount;
    final private PaymentTransactionRepository transactionRepository;
    final private PaymentTransactionServerService transactionService;

    public DailyBonusService(PaymentTransactionRepository transactionRepository,
            PaymentTransactionServerService transactionService,
            Money bonusAmount) {
        this.transactionRepository = checkNotNull(transactionRepository);
        this.transactionService = checkNotNull(transactionService);
        this.bonusAmount = checkNotNull(bonusAmount);
    }

    @Override
    public void entered(String player) {
        // Step 1. Generating latest transaction id
        PaymentTransactionKey transactionKey = new PaymentTransactionKey(MoneySource.dailybonus, player);
        PaymentTransaction lastBonus = transactionRepository.findOne(transactionKey);
        // Step 2. Checking player eligible for bonus
        if (lastBonus == null || TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - lastBonus.getTransactionDate().getTime()) > 0){
            // Step 3. Updating transaction
            if(lastBonus != null) {
                lastBonus.getTransactionKey().setSource(transactionKey.getSource() + "_" + DATE_FORMAT.format(lastBonus.getTransactionDate()));
                transactionRepository.saveAndFlush(lastBonus);
            }
            // Step 4. Creating new transaction
            PaymentTransaction bonusTransaction = new PaymentTransaction()
                .setTransactionKey(transactionKey).setTransactionDate(new Date())
                .addPaymentOperation(new PaymentOperation(PlayerAware.DEFAULT_PLAYER, bonusAmount, Operation.Credit))
                .addPaymentOperation(new PaymentOperation(player, bonusAmount, Operation.Debit));
            // Step 5. Processing transaction
            transactionService.process(bonusTransaction);
        }
    }

}
