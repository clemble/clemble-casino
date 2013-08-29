package com.gogomaya.money;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

import com.gogomaya.server.hibernate.ImmutableHibernateType;

public class MoneyHibernate extends ImmutableHibernateType<Money> {

    final int[] TYPES = new int[] { Types.INTEGER, Types.BIGINT };

    @Override
    public int[] sqlTypes() {
        return TYPES;
    }

    @Override
    public Class<Money> returnedClass() {
        return Money.class;
    }

    @Override
    public Object nullSafeGet(final ResultSet rs, final String[] names, final SessionImplementor session, final Object owner) throws HibernateException,
            SQLException {
        Currency currency = Currency.class.getEnumConstants()[rs.getInt(names[0])];
        return Money.create(currency, rs.getLong(names[1]));
    }

    @Override
    public void nullSafeSet(final PreparedStatement st, final Object value, int index, final SessionImplementor session) throws HibernateException,
            SQLException {
        Money money = (Money) value;

        st.setInt(index++, money.getCurrency().ordinal());
        st.setLong(index++, money.getAmount());
    }

}