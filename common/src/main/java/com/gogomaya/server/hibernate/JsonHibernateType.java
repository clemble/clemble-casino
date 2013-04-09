package com.gogomaya.server.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.ParameterizedType;

public class JsonHibernateType<T extends Serializable> extends ImmutableHibernateType<T> implements ParameterizedType {

    final public static String CLASS_NAME_PARAMETER = "className";

    final private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    final private static int[] TYPES = new int[] { Types.VARCHAR };

    private Class<T> targetClass;

    @Override
    public int[] sqlTypes() {
        return TYPES;
    }

    @Override
    public Class<T> returnedClass() {
        return targetClass;
    }

    @Override
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

    @Override
    @SuppressWarnings({ "static-access", "unchecked" })
    public synchronized void setParameterValues(final Properties parameters) {
        if (parameters.contains(CLASS_NAME_PARAMETER))
            throw new IllegalArgumentException("Class name not provided");
        String className = parameters.get(CLASS_NAME_PARAMETER).toString();
        try {
            targetClass = (Class<T>) getClass().forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
