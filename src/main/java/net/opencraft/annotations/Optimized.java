package net.opencraft.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation indicates if a method was already optimized or if it needs
 * optimization.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Optimized {

	boolean value() default true;

}
