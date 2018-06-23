package com.singingbush.dubclient.data;

import org.jetbrains.annotations.Nullable;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

/**
 * @author Samael Bate (singingbush)
 * created on 23/06/18
 */
class Utils {

    private static final DateTimeFormatter LENIENT_ISO_8601 = new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .append(ISO_LOCAL_DATE_TIME)
        .appendOffset("+HH:MM:ss", "")
        .toFormatter();

    /**
     * The API returns UTC datetime values but doesn't always include the 'Z' for the UTC zone.
     * This method can handle "yyyy-MM-dd'T'HH:mm:ssZ" or "yyyy-MM-dd'T'HH:mm:ss"
     * @param txt an ISO-8601 datetime String
     * @return a ZonedDateTime or null
     */
    @Nullable
    static ZonedDateTime parseDatetime(@Nullable final String txt) {
        if(txt != null && !txt.isEmpty()) {
            return OffsetDateTime.parse(txt.replace("Z", ""), LENIENT_ISO_8601).toZonedDateTime();
        }
        return null;
    }
}
