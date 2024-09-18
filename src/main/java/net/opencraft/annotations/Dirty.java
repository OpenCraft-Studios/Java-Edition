package net.opencraft.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Dirty code that could cause problems in the future due to difficult
 * maintenance or unclear purpose.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Dirty {

}
