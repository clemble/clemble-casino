package com.gogomaya.server.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

abstract public class JsonHibernateType<T extends Serializable> extends ImmutableHibernateType<T> {

    final private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    final private static int[] TYPES = new int[] { Types.VARCHAR };

    @Override
    public int[] sqlTypes() {
        return TYPES;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        String jsonPresentation = rs.getString(names[0]);
        T result = null;
        try {
            result = (T) OBJECT_MAPPER.readValue(jsonPresentation, returnedClass());
        } catch (Exception ignore) {
        }
        return result;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        String jsonPresentation = "";
        try {
            if (value != null)
                jsonPresentation = OBJECT_MAPPER.writeValueAsString(value);
        } catch (Exception ignore) {
        }
        st.setString(index, jsonPresentation);
    }
}
