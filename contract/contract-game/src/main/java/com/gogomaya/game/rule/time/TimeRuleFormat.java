package com.gogomaya.game.rule.time;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

import com.gogomaya.server.hibernate.ImmutableHibernateType;

public class TimeRuleFormat {

    public static class MoveTimeRuleHibernateType extends ImmutableHibernateType<TimeRule> {

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
            return new MoveTimeRule().setPunishment(breachBehavior).setLimit(rs.getInt(names[2]));
        }

        @Override
        public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
            MoveTimeRule timeRule = (MoveTimeRule) value;
            st.setString(index++, timeRule.getPunishment().name());
            st.setLong(index++, ((MoveTimeRule) timeRule).getLimit());
        }

    }

    public static class TotalTimeRuleHibernateType extends ImmutableHibernateType<TimeRule> {

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

}
