package com.gogomaya.server.game.time.rule;

import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

import com.gogomaya.server.buffer.ByteBufferStream;
import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.hibernate.ImmutableHibernateType;

public class TimeLimitRuleFormat {

    public static enum TimeRuleType {

        None,
        Total,
        Move;

        public static TimeRuleType valueOf(Class<? extends TimeLimitRule> timeLimitClass) {
            if (TimeLimitMoveRule.class.equals(timeLimitClass)) {
                return Move;
            } else if (TimeLimitNoneRule.class.equals(timeLimitClass)) {
                return None;
            } else if (TimeLimitTotalRule.class.equals(timeLimitClass)) {
                return Total;
            }

            return null;
        }

    }

    public static class TimeRuleHibernateType extends ImmutableHibernateType<TimeLimitRule> {

        final private int[] TYPES = { Types.VARCHAR, Types.VARCHAR, Types.INTEGER };

        @Override
        public int[] sqlTypes() {
            return TYPES;
        }

        @Override
        public Class<?> returnedClass() {
            return TimeLimitRule.class;
        }

        @Override
        public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
            TimeRuleType ruleType = TimeRuleType.valueOf(rs.getString(names[0]));
            TimeBreachBehavior breachBehavior = TimeBreachBehavior.valueOf(rs.getString(names[1]));

            switch (ruleType) {
            case Total:
                return TimeLimitTotalRule.create(breachBehavior, rs.getInt(names[2]));
            case Move:
                return TimeLimitMoveRule.create(breachBehavior, rs.getInt(names[2]));
            case None:
                return TimeLimitNoneRule.INSTANCE;
            }
            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        }

        @Override
        public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
            TimeLimitRule timeRule = value != null ? ((TimeLimitRule) value) : TimeLimitRule.DEFAULT;
            TimeRuleType timeRuleType = TimeRuleType.valueOf(timeRule.getClass());
            st.setString(index++, timeRuleType.name());
            st.setString(index++, timeRule.getBreachBehavior().name());
            switch (timeRuleType) {
            case Total:
                st.setInt(index++, ((TimeLimitTotalRule) timeRule).getGameTimeLimit());
                break;
            case Move:
                st.setInt(index++, ((TimeLimitMoveRule) timeRule).getMoveTimeLimit());
                break;
            case None:
                st.setInt(index++, 0);
                break;

            }
        }

    }

    public static class CustomTimeRuleByteBufferStream implements ByteBufferStream<TimeLimitRule> {

        @Override
        public ByteBuffer write(TimeLimitRule timeRule, ByteBuffer writeBuffer) {
            TimeRuleType timeRuleType = TimeRuleType.valueOf(timeRule.getClass());
            writeBuffer.put((byte) timeRuleType.ordinal()).put((byte) timeRule.getBreachBehavior().ordinal());

            switch (timeRuleType) {
            case Total:
                writeBuffer.putInt(((TimeLimitTotalRule) timeRule).getGameTimeLimit());
                break;
            case Move:
                writeBuffer.putInt(((TimeLimitMoveRule) timeRule).getMoveTimeLimit());
                break;
            case None:
                break;
            default:
                throw GogomayaException.create(GogomayaError.ServerCriticalError);
            }

            return writeBuffer;
        }

        @Override
        public TimeLimitRule read(ByteBuffer readBuffer) {
            byte timeRule = readBuffer.get();
            TimeRuleType timeRuleType = timeRule == TimeRuleType.Total.ordinal() ? TimeRuleType.Total
                    : timeRule == TimeRuleType.Move.ordinal() ? TimeRuleType.Move : timeRule == TimeRuleType.None.ordinal() ? TimeRuleType.None : null;

            byte breachType = readBuffer.get();
            TimeBreachBehavior breachBehavior = breachType == TimeBreachBehavior.nothing.ordinal() ? TimeBreachBehavior.nothing
                    : breachType == TimeBreachBehavior.loose.ordinal() ? TimeBreachBehavior.loose : null;

            switch (timeRuleType) {
            case Total:
                return TimeLimitTotalRule.create(breachBehavior, readBuffer.getInt());
            case Move:
                return TimeLimitMoveRule.create(breachBehavior, readBuffer.getInt());
            case None:
                return TimeLimitNoneRule.INSTANCE;
            default:
                throw GogomayaException.create(GogomayaError.ServerCriticalError);
            }
        }

    }
}
