package com.clemble.casino.server.payment.repository;

import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.payment.money.Currency;
import com.clemble.casino.payment.money.Money;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        return new PlayerAccount(player, cash);
    }

    @Override
    public void debit(String player, Money amount) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.incrBy(player + amount.getCurrency(), amount.getAmount());
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    @Override
    public void credit(String player, Money amount) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.decrBy(player + amount.getCurrency(), amount.getAmount());
        } finally {
            jedisPool.returnResource(jedis);
        }
    }
}
