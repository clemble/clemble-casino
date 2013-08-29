package com.gogomaya.server.hibernate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

import com.google.common.collect.ImmutableMap;

public class EnumStringHibernateType<T extends Enum<T>> extends ImmutableHibernateType<T> {

    final private int[] TYPES = new int[] { Types.VARCHAR };
    final private Class<T> ENUM_CLASS;
    final private ImmutableMap<String, Enum<?>> ENUM_MAP;

    public EnumStringHibernateType(Class<T> enumClass) {
        this.ENUM_CLASS = enumClass;

        Map<String, Enum<?>> mapValues = new HashMap<String, Enum<?>>();
        for (Enum<?> enumValue : enumClass.getEnumConstants()) {
            mapValues.put(enumValue.name(), enumValue);
        }
        this.ENUM_MAP = ImmutableMap.<String, Enum<?>> copyOf(mapValues);
    }

    @Override
    public int[] sqlTypes() {
        return this.TYPES;
    }

    @Override
    public Class<?> returnedClass() {
        return this.ENUM_CLASS;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        return ENUM_MAP.get(rs.getString(names[0]));
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        st.setString(index, ((Enum<?>) value).name());
    }

}
