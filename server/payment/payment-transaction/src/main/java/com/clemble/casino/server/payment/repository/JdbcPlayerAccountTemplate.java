package com.clemble.casino.server.payment.repository;

import com.clemble.casino.payment.PendingOperation;
import com.clemble.casino.payment.PendingTransaction;
import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.server.payment.MoneyRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

/**
 * Created by mavarazy on 21/02/14.
 */
public class JdbcPlayerAccountTemplate implements PlayerAccountTemplate {

    final private JdbcTemplate jdbc;

    final private String FIND_ONE = "SELECT CURRENCY, AMOUNT FROM PLAYER_ACCOUNT_AMOUNT WHERE PLAYER = ?";
    final private String UPDATE_QUERY = "SELECT AMOUNT FROM PLAYER_ACCOUNT_AMOUNT WHERE PLAYER = ? AND CURRENCY = ?";
    final private String UPDATE_ALTER = "UPDATE PLAYER_ACCOUNT_AMOUNT SET AMOUNT = ? WHERE PLAYER = ? AND CURRENCY = ? AND AMOUNT = ?";
    final private String INSERT = "INSERT INTO PLAYER_ACCOUNT_AMOUNT(PLAYER, CURRENCY, AMOUNT) VALUES (?, ?, ?)";

    public JdbcPlayerAccountTemplate(JdbcTemplate template) {
        this.jdbc = checkNotNull(template);
    }

    @Override
    public PlayerAccount findOne(String player){
        Map<Currency, Money> cash = new HashMap<>();
        for(Money amount: jdbc.query(FIND_ONE, MoneyRowMapper.INSTANCE))
            cash.put(amount.getCurrency(), amount);
        return new PlayerAccount(player, cash, Collections.<PendingOperation>emptyList(), 0);
    }

    // TODO this is really bad solution switch to Actors model
    @Override
    public void debit(String player, String transactionKey, Money amount) {
        int linesUpdated = 0;
        do {
            try {
                Integer playerBalance = jdbc.queryForObject(UPDATE_QUERY, Integer.class, player, amount.getCurrency().ordinal());
                linesUpdated = jdbc.update(UPDATE_ALTER, playerBalance + amount.getAmount(), player, amount.getCurrency().ordinal(), playerBalance);
            } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
                // TODO SECURITY - you can construct players, that are not in the system and process requests for them
                try {
                    jdbc.update(INSERT, player, amount.getCurrency().ordinal(), 0);
                } catch (Throwable throwable) {
                    // TODO add some kind of notification
                }
            }
        } while(linesUpdated == 0);
    }

    @Override
    public void credit(String player, String transactionKey, Money amount) {
        debit(player, transactionKey, amount.negate());
    }

    @Override
    public PendingTransaction freeze(PendingTransaction pendingTransaction) {
        throw new IllegalAccessError();
    }

}
