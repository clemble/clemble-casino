package com.gogomaya.server.game.rule.bet;

import static com.google.common.base.Preconditions.checkNotNull;

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

public class BetRuleFormat {

    public static enum BetType {

        NONE,
        FIXED,
        LIMITED;

        public static BetType valueOf(Class<? extends BetRule> betRuleClass) {
            if (betRuleClass.equals(FixedBetRule.class)) {
                return FIXED;
            } else if (betRuleClass.equals(LimitedBetRule.class)) {
                return LIMITED;
            } else if (betRuleClass.equals(UnlimitedBetRule.class)) {
                return NONE;
            }
            return null;
        }

    }

    public static class BetRuleHibernateType extends ImmutableHibernateType<BetRule> {
        final private int[] TYPES = { Types.VARCHAR, Types.INTEGER, Types.INTEGER };

        @Override
        public int[] sqlTypes() {
            return TYPES;
        }

        @Override
        public Class<?> returnedClass() {
            return BetRule.class;
        }

        @Override
        public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
            BetType betType = BetType.valueOf(rs.getString(names[0]));
            switch (betType) {
            case FIXED:
                return new FixedBetRule(rs.getInt(names[1]));
            case LIMITED:
                return LimitedBetRule.create(rs.getInt(names[1]), rs.getInt(names[2]));
            case NONE:
                return UnlimitedBetRule.INSTANCE;
            }
            throw GogomayaException.fromError(GogomayaError.ServerCriticalError);
        }

        @Override
        public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
            BetRule betRule = (BetRule) checkNotNull(value);
            BetType betType = BetType.valueOf(betRule.getClass());
            st.setString(index++, betType.name());
            switch (betType) {
            case FIXED:
                st.setLong(index++, ((FixedBetRule) value).getBet());
                st.setLong(index++, ((FixedBetRule) value).getBet());
                break;
            case LIMITED:
                st.setLong(index++, ((LimitedBetRule) value).getMinBet());
                st.setLong(index++, ((LimitedBetRule) value).getMaxBet());
                break;
            case NONE:
                st.setLong(index++, 0);
                st.setLong(index++, 0);
                break;
            default:
                throw GogomayaException.fromError(GogomayaError.ServerCriticalError);
            }
        }

    }

    public static class BetRuleByteBufferStream implements ByteBufferStream<BetRule> {

        @Override
        public ByteBuffer write(BetRule betRule, ByteBuffer buffer) {
            BetType betType = BetType.valueOf(betRule.getClass());

            buffer.put((byte) betType.ordinal());

            switch (betType) {
            case FIXED:
                buffer.putInt(((FixedBetRule) betRule).getBet());
                break;
            case LIMITED:
                buffer.putInt(((LimitedBetRule) betRule).getMinBet()).putInt(((LimitedBetRule) betRule).getMaxBet());
                break;
            case NONE:
                break;
            }
            return buffer;
        }

        @Override
        public BetRule read(ByteBuffer buffer) {
            int betType = buffer.get();

            if (betType == BetType.FIXED.ordinal()) {
                return new FixedBetRule(buffer.getInt());
            } else if (betType == BetType.LIMITED.ordinal()) {
                return LimitedBetRule.create(buffer.getInt(), buffer.getInt());
            } else if (betType == BetType.NONE.ordinal()) {
                return UnlimitedBetRule.INSTANCE;
            }

            throw GogomayaException.fromError(GogomayaError.ServerCriticalError);
        }
    }

}
