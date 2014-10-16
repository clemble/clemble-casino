package com.clemble.casino.server.payment.repository;

import com.clemble.casino.money.Operation;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PendingOperation;
import com.clemble.casino.payment.PendingTransaction;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mavarazy on 21/02/14.
 */
public class JedisPlayerAccountTemplate implements PlayerAccountTemplate {

    final private JedisPool jedisPool;

    public JedisPlayerAccountTemplate(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public PlayerAccount findOne(String player) {
        Map<Currency, Money> cash = new HashMap<Currency, Money>();
        Jedis jedis = jedisPool.getResource();
        try {
            for(Currency currency: Currency.values()) {
                String amount = jedis.get(player + currency);
                if (amount != null) {
                    cash.put(currency, new Money(currency, Long.valueOf(amount)));
                } else {
                    cash.put(currency, new Money(currency, 0));
                }
            }
        } finally {
            jedisPool.returnResource(jedis);
        }
        return new PlayerAccount(player, cash, Collections.<PendingOperation>emptyList(), 0);
    }

    @Override
    public PlayerAccount process(String transactionKey, PaymentOperation operation) {
        Money amount = operation.getOperation() == Operation.Debit ? operation.getAmount() : operation.getAmount().negate();
        debit(operation.getPlayer(), transactionKey, amount);
        return findOne(operation.getPlayer());
    }

    public void debit(String player, String transactionKey, Money amount) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.incrBy(player + amount.getCurrency(), amount.getAmount());
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    @Override
    public PendingTransaction freeze(PendingTransaction pendingTransaction) {
        throw new IllegalAccessError();
    }

}
