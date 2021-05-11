package com.singingbush.dubclient.data;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Samael Bate (singingbush)
 * created on 23/06/18
 */
public class UtilsTest {

    @DisplayName("testing Utils.parseDatetime")
    @Test //todo: make this a parameterised test
    public void parseDatetime() {
        assertNull(Utils.parseDatetime(null));
        assertNull(Utils.parseDatetime(""));

        final ZonedDateTime dt1 = Utils.parseDatetime("2017-09-21T18:45:00Z");
        assertNotNull(dt1);
        assertEquals(ZonedDateTime.parse("2017-09-21T18:45:00Z"), dt1);

        final ZonedDateTime dt2 = Utils.parseDatetime("2017-09-21T18:45:00");
        assertNotNull(dt2);
        assertEquals(ZonedDateTime.parse("2017-09-21T18:45:00Z"), dt2);

        assertEquals(dt1, dt2, "both datetime values should be the same");
    }
}
