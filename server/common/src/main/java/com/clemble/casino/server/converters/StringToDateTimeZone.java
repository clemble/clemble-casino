package com.clemble.casino.server.converters;

import org.joda.time.DateTimeZone;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * Created by mavarazy on 2/19/15.
 */
@ReadingConverter
public class StringToDateTimeZone implements Converter<String, DateTimeZone> {

    @Override
    public DateTimeZone convert(String id) {
        return DateTimeZone.forID(id);
    }

}
