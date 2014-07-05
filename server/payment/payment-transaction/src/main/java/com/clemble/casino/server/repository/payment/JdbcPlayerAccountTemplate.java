package com.clemble.casino.server.repository.payment;

import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.server.payment.MoneyRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

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
        return new PlayerAccount(player, jdbc.query(FIND_ONE, MoneyRowMapper.INSTANCE, player));
    }

    // TODO this is really bad solution switch to Actors model
    @Override
    public void debit(String player, Money amount) {
        int linesUpdated = 0;
        do {
            try {
                Integer playerBalance = jdbc.queryForObject(UPDATE_QUERY, Integer.class, player, amount.getCurrency().ordinal());
                linesUpdated = jdbc.update(UPDATE_ALTER, playerBalance + amount.getAmount(), player, amount.getCurrency().ordinal(), playerBalance);
            } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
                // TODO SECURITY - you can create players, that are not in the system and process requests for them
                try {
                    jdbc.update(INSERT, player, amount.getCurrency().ordinal(), 0);
                } catch (Throwable throwable) {
                    // TODO add some kind of notification
                }
            }
        } while(linesUpdated == 0);
    }

    @Override
    public void credit(String player, Money amount) {
        debit(player, amount.negate());
    }
}
