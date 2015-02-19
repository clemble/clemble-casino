package com.clemble.casino.server.converters;

import org.joda.time.tz.FixedDateTimeZone;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * Created by mavarazy on 2/19/15.
 */
@WritingConverter
public class FixedDateTimeZoneToString implements Converter<FixedDateTimeZone, String> {

    @Override
    public String convert(FixedDateTimeZone source) {
        return source.getID();
    }

}
