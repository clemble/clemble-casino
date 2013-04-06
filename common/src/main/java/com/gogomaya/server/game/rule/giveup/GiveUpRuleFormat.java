package com.gogomaya.server.game.rule.giveup;

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

public class GiveUpRuleFormat {

    final public static GiveUpRule DEFAULT_GIVE_UP_RULE = GiveUpAllRule.INSTANCE;

    public enum LoosingType {

        All,
        Lost,
        MinPart;

        public static LoosingType valueOf(Class<? extends GiveUpRule> giveUpClass) {
            if (giveUpClass.equals(GiveUpAllRule.class)) {
                return All;
            } else if (giveUpClass.equals(GiveUpLostRule.class)) {
                return Lost;
            } else if (giveUpClass.equals(GiveUpLeastRule.class)) {
                return MinPart;
            }
            return null;
        }
    }

    public static class GiveUpRuleHibernateType extends ImmutableHibernateType<GiveUpRule> {

        final private int[] TYPES = { Types.VARCHAR, Types.INTEGER };

        @Override
        public int[] sqlTypes() {
            return TYPES;
        }

        @Override
        public Class<?> returnedClass() {
            return GiveUpRule.class;
        }

        @Override
        public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
            LoosingType loosingType = LoosingType.valueOf(rs.getString(names[0]));
            switch (loosingType) {
            case All:
                return GiveUpAllRule.INSTANCE;
            case Lost:
                return GiveUpLostRule.INSTANCE;
            case MinPart:
                return GiveUpLeastRule.create(rs.getInt(names[1]));
            }
            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        }

        @Override
        public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
            GiveUpRule giveUpRule = value != null ? (GiveUpRule) value : DEFAULT_GIVE_UP_RULE;
            LoosingType loosingType = LoosingType.valueOf(giveUpRule.getClass());
            st.setString(index++, loosingType.name());
            st.setInt(index, value instanceof GiveUpLeastRule ? ((GiveUpLeastRule) value).getMinPart() : 0);
        }

    }

    public static class CustomGiveUpRuleByteBufferStream implements ByteBufferStream<GiveUpRule> {

        @Override
        public ByteBuffer write(GiveUpRule value, ByteBuffer writeBuffer) {
            LoosingType loosingType = LoosingType.valueOf(value.getClass());
            writeBuffer.put((byte) loosingType.ordinal());

            switch (loosingType) {
            case MinPart:
                writeBuffer.putInt(((GiveUpLeastRule) value).getMinPart());
                break;
            case All:
            case Lost:
                break;
            }

            return writeBuffer;
        }

        @Override
        public GiveUpRule read(ByteBuffer readBuffer) {
            int loosingType = (int) readBuffer.get();

            if (loosingType == LoosingType.All.ordinal()) {
                return GiveUpAllRule.INSTANCE;
            } else if (loosingType == LoosingType.Lost.ordinal()) {
                return GiveUpLostRule.INSTANCE;
            } else if (loosingType == LoosingType.MinPart.ordinal()) {
                return GiveUpLeastRule.create(readBuffer.getInt());
            }

            throw GogomayaException.create(GogomayaError.ServerCriticalError);
        }

    }
}
