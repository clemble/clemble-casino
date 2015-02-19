package com.clemble.casino.server.converters;

import org.joda.time.DateTimeZone;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * Created by mavarazy on 2/19/15.
 */
@WritingConverter
public class DateTimeZoneToString implements Converter<DateTimeZone, String> {

    @Override
    public String convert(DateTimeZone source) {
        return source.getID();
    }

}
