package at.kaindorf.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies the format used for Date serialization and deserialization.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonFormat {
    /**
     * Specifies the format used for Date serialization and deserialization.
     */
    String value() default "";
}