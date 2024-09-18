package net.opencraft.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface UsesReflection {

	/**
	 * This should have the fields, methods and classes that could be affected by
	 * the reflection.
	 */
	String[] codeAffected();

}
