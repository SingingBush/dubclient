package com.singingbush.dubclient;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class DubClientBuilderTest {

    @DisplayName("build DubClient with default values")
    @Test
    void builderWithDefaultValues() {
        final DubClient client = DubClient.builder().build();

        assertPrivateFieldEquals(client, "scheme", "https");
        assertPrivateFieldEquals(client, "hostName", "code.dlang.org");
        assertPrivateFieldEquals(client, "port", 443);
    }

    @DisplayName("build DubClient with custom values")
    @Test
    void builderWithCustomValues() {
        final DubClient client = DubClient.builder()
            .withScheme("http")
            .withHostname("localhost")
            .withPort(8080)
            .build();

        assertPrivateFieldEquals(client, "scheme", "http");
        assertPrivateFieldEquals(client, "hostName", "localhost");
        assertPrivateFieldEquals(client, "port", 8080);
    }

    private void assertPrivateFieldEquals(@NotNull Object target, @NotNull String name, @NotNull Object expectedValue) {
        try {
            final Field field = target.getClass().getDeclaredField(name);
            field.setAccessible(true);
            assertEquals(expectedValue, field.get(target));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getMessage());
        }
    }
}
