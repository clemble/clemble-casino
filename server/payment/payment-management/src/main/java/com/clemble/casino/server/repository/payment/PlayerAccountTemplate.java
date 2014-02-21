package com.clemble.casino.server.repository.payment;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.clemble.casino.payment.PlayerAccount;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.server.payment.MoneyRowMapper;

public interface PlayerAccountTemplate {

    public PlayerAccount findOne(String player);

    // TODO this is really bad solution switch to Actors model
    public void debit(String player, Money amount);

    public void credit(String player, Money amount);

}
