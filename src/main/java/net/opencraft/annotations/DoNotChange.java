package net.opencraft.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This field/method or class is being used by some method that uses reflection.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface DoNotChange {
}
