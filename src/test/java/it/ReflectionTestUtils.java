package it;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * @author Samael Bate (singingbush)
 * created on 22/06/18
 */
public class ReflectionTestUtils {

    /**
     * @param target the target object on which to set the field
     * @param name the name of the field to set
     * @param value the value to set
     */
    public static void setField(@NotNull Object target, @NotNull String name, @NotNull Object value) {
        try {
            final Field field = target.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getMessage());
        }
    }

    public static class Assert {

        static public void assertAllGettersNotNull(final Object obj) {
            assertAllGettersNotNull(null, obj);
        }

        /**
         * Asserts that all the public getters on an object return non-null.
         * If a getter returns null a {@link AssertionError} is thrown with the given message.
         *
         * @param message the identifying message for the {@link AssertionError} (<code>null</code> okay)
         * @param obj the value to check against <code>unexpected</code>
         */
        static public void assertAllGettersNotNull(final String message, final Object obj) {
            final Method[] methods = obj.getClass().getDeclaredMethods();

            Arrays.stream(methods).forEach(m -> {
                if(m.getParameterTypes().length == 0 && m.getName().startsWith("get")) {
                    final String msg = message != null ? message : m.getName() + "() returned null";
                    try {
                        assertNotNull(msg, m.invoke(obj, null));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        fail(e.getMessage());
                    }
                }
            });
        }
    }
}
