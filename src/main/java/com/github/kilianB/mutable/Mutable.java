package com.github.kilianB.mutable;

/**
 * Mutable class wrapper for basic java data types. Mutable classes are useful
 * in lambda expressions or anonymous classes which want to alter the content of
 * a variable but are limited to final or effective final variables.
 * 
 * @author Kilian
 */
public interface Mutable<T> {
	/**
	 * Get an object encapsulating the current value
	 * 
	 * @return The current value as an immutable base object
	 */
	T getValue();
	

	void setValue(T newValue);
}
