package com.clemble.casino.game.rule.time;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

import com.clemble.casino.server.hibernate.ImmutableHibernateType;

public class TotalTimeRuleHibernate extends ImmutableHibernateType<TimeRule> {

    final private int[] TYPES = { Types.VARCHAR, Types.INTEGER };

    @Override
    public int[] sqlTypes() {
        return TYPES;
    }

    @Override
    public Class<?> returnedClass() {
        return TimeRule.class;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        TimeBreachPunishment breachBehavior = TimeBreachPunishment.valueOf(rs.getString(names[1]));
        return new TotalTimeRule().setPunishment(breachBehavior).setLimit(rs.getInt(names[2]));
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        TotalTimeRule timeRule = (TotalTimeRule) value;
        st.setString(index++, timeRule.getPunishment().name());
        st.setLong(index++, ((TotalTimeRule) timeRule).getLimit());
    }

}
