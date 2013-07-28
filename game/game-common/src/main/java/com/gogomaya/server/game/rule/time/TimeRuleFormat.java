package com.gogomaya.server.game.rule.time;

import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

import com.gogomaya.server.buffer.ByteBufferStream;
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

    public static class MoveTimeRuleByteBufferStream implements ByteBufferStream<MoveTimeRule> {

        @Override
        public ByteBuffer write(MoveTimeRule timeRule, ByteBuffer writeBuffer) {
            writeBuffer.put((byte) timeRule.getPunishment().ordinal());
            writeBuffer.putLong(((MoveTimeRule) timeRule).getLimit());
            return writeBuffer;
        }

        @Override
        public MoveTimeRule read(ByteBuffer readBuffer) {
            TimeBreachPunishment breachBehavior = TimeBreachPunishment.values()[readBuffer.get()];
            return new MoveTimeRule().setPunishment(breachBehavior).setLimit(readBuffer.getInt());
        }

    }

    public static class TotalTimeRuleByteBufferStream implements ByteBufferStream<TotalTimeRule> {

        @Override
        public ByteBuffer write(TotalTimeRule totalTimeRule, ByteBuffer writeBuffer) {
            writeBuffer.put((byte) totalTimeRule.getPunishment().ordinal());
            writeBuffer.putLong(totalTimeRule.getLimit());
            return writeBuffer;
        }

        @Override
        public TotalTimeRule read(ByteBuffer readBuffer) {
            return new TotalTimeRule().setPunishment(TimeBreachPunishment.values()[readBuffer.get()]).setLimit(readBuffer.getInt());
        }

    }
}
