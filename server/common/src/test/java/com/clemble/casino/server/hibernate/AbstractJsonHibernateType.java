package com.clemble.casino.server.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import com.clemble.casino.json.CustomJacksonAnnotationIntrospector;
import com.clemble.casino.json.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AbstractJsonHibernateType<T extends Serializable> implements UserType {

    final private Class<T> targetClass;
    final private static ObjectMapper OBJECT_MAPPER;
    final private static int[] TYPES = new int[] { Types.VARCHAR };

    static {
        OBJECT_MAPPER = ObjectMapperUtils.createObjectMapper();
        OBJECT_MAPPER.setAnnotationIntrospector(new CustomJacksonAnnotationIntrospector());
    }

    protected AbstractJsonHibernateType(Class<T> targetClasss) {
        this.targetClass = targetClasss;
    }


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
            if (jsonPresentation != null && jsonPresentation.length() > 0)
                result = (T) OBJECT_MAPPER.readValue(jsonPresentation, returnedClass());
        } catch (Throwable ignore) {
            System.out.println(jsonPresentation);
            ignore.printStackTrace();
        }
        return result;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        String jsonPresentation = "";
        try {
            if (value != null)
                jsonPresentation = OBJECT_MAPPER.writeValueAsString(value);
        } catch (Throwable ignore) {
            ignore.printStackTrace();
        }
        st.setString(index, jsonPresentation);
    }

    @Override
    final public boolean equals(Object x, Object y) throws HibernateException {
        return x != null ? x.equals(y) : y == null;
    }

    @Override
    final public int hashCode(Object x) throws HibernateException {
        return x != null ? x.hashCode() : 0;
    }

    @Override
    final public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    final public boolean isMutable() {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    final public Serializable disassemble(Object value) throws HibernateException {
        return (T) value;
    }

    @Override
    final public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

}
