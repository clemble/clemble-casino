package com.clemble.casino.server.payment;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.clemble.casino.payment.money.Currency;
import com.clemble.casino.payment.money.Money;

public class MoneyRowMapper implements RowMapper<Money>{

    final public static RowMapper<Money> INSTANCE = new MoneyRowMapper();

    @Override
    public Money mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Money.create(Currency.values()[rs.getInt("CURRENCY")], rs.getLong("AMOUNT"));
    }

}
