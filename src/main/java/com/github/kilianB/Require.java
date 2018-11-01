package com.github.kilianB;

import java.util.Collection;

/**
 * @author Kilian
 *
 */
public class Require {

	/**
	 * Checks if the supplied argument is a positive non null numeric value and
	 * throws a IllegalArgumentException if it isn't
	 * 
	 * @param value to be checked
	 * @return The supplied value
	 * @param <T> the type of the value
	 * @since 1.0.0
	 */
	public static <T extends Number> T positiveValue(T value) {
		return positiveValue(value, null);
	}

	/**
	 * Checks if the supplied argument is a positive non null numeric value and
	 * throws a IllegalArgumentException if it isn't
	 * 
	 * @param value   to be checked
	 * @param message to be thrown in case of error
	 * @return The supplied value
	 * @param <T> the type of the value
	 * @since 1.0.0
	 */
	public static <T extends Number> T positiveValue(T value, String message) {
		if (value.doubleValue() <= 0) {
			if (message == null) {
				throw new IllegalArgumentException();
			} else {
				throw new IllegalArgumentException(message);
			}
		}
		return value;
	}

	/**
	 * Checks if the supplied argument is lays within the given bounds throws a
	 * IllegalArgumentException if it doesn't
	 * 
	 * @param value       to be checked
	 * @param lowerBound  inclusively
	 * @param higherBound inclusively
	 * @param message     to be thrown in case of error
	 * @param             <T> the type of the value
	 * @return The supplied value
	 * @since 1.0.0
	 */
	public static <T extends Number> T inRange(T value, T lowerBound, T higherBound, String message) {
		if (value.doubleValue() < lowerBound.doubleValue() || value.doubleValue() > higherBound.doubleValue()) {
			if (message == null) {
				throw new IllegalArgumentException();
			} else {
				throw new IllegalArgumentException(message);
			}
		}
		return value;
	}
	
	/**
	 * Checks if the supplied argument is lays within the given bounds throws a
	 * IllegalArgumentException if it doesn't
	 * 
	 * @param value       to be checked
	 * @param lowerBound  inclusively
	 * @param higherBound inclusively
	 * @param message     to be thrown in case of error
	 * @param             <T> the type of the value
	 * @return The supplied value
	 * @since 1.1.0
	 */
	public static <T extends Number> Collection<T> inRange(Collection<T> value, T lowerBound, T higherBound, String message) {
		for(T t : value) {
			inRange(t,lowerBound,higherBound,message);
		}
		return value;
	}
	

	/**
	 * Checks if the supplied argument is lays within the given bounds throws a
	 * IllegalArgumentException if it doesn't
	 * 
	 * @param value       to be checked
	 * @param lowerBound  inclusively
	 * @param higherBound inclusively
	 * @param             <T> the type of the value
	 * @return The supplied value
	 * @since 1.0.0
	 */
	public static <T extends Number> T inRange(T value, T lowerBound, T higherBound) {
		return inRange(value, lowerBound, higherBound, null);
	}

	/**
	 * Checks if the supplied array does not contain null values and throws a
	 * IllegalArgumentException if it does.
	 * 
	 * @param array to be checked
	 * @param       <T> the type of the value
	 * @return The supplied value
	 * @since 1.0.0
	 */
	public static <T> T[] nonNull(T[] array) {
		return nonNull(array, null);
	}

	/**
	 * Checks if the supplied array does not contain null values and throws a
	 * IllegalArgumentException if it does.
	 * 
	 * @param array   to be checked
	 * @param message message to be thrown in case of failure
	 * @param         <T> the type of the value
	 * @return The supplied value
	 * @since 1.0.0
	 */
	public static <T> T[] nonNull(T[] array, String message) {
		if (ArrayUtil.allNotNull(array)) {
			return array;
		} else {
			if (message == null) {
				throw new IllegalArgumentException();
			} else {
				throw new IllegalArgumentException(message);
			}
		}
	}

	/**
	 * Deeply checks if the supplied array does not contain null values and throws a
	 * IllegalArgumentException if it does.
	 * <p>
	 * 
	 * This method supports nested arrays.
	 * 
	 * @param array   to be checked
	 * @param         <T> the type of the value
	 * @return The supplied value
	 * @since 1.0.0
	 */
	public static <T> T[] deepNonNull(T[] array) {
		return deepNonNull(array, null);
	}

	/**
	 * Deeply checks if the supplied array does not contain null values and throws a
	 * IllegalArgumentException if it does.
	 * <p>
	 * 
	 * This method supports nested arrays.
	 * 
	 * @param array   to be checked
	 * @param message message to be thrown in case of failure
	 * @param         <T> the type of the value
	 * @return The supplied value
	 * @since 1.0.0
	 */
	public static <T> T[] deepNonNull(T[] array, String message) {
		if (ArrayUtil.deepAllNotNull(array)) {
			return array;
		} else {
			if (message == null) {
				throw new IllegalArgumentException();
			} else {
				throw new IllegalArgumentException(message);
			}
		}
	}

	/**
	 * Checks if the supplied array does only contain null values and throws a
	 * IllegalArgumentException if it does not..
	 * 
	 * @param array to be checked
	 * @param       <T> the type of the value
	 * @return The supplied value
	 * @since 1.0.0
	 */
	public static <T> T[] allNull(T[] array) {
		return allNull(array, null);
	}

	/**
	 * Checks if the supplied array does only contain null values and throws a
	 * IllegalArgumentException if it does not..
	 * 
	 * @param array   to be checked
	 * @param message message to be thrown in case of failure
	 * @param         <T> the type of the value
	 * @return The supplied value
	 * @since 1.0.0
	 */
	public static <T> T[] allNull(T[] array, String message) {
		if (ArrayUtil.allNull(array)) {
			return array;
		} else {
			if (message == null) {
				throw new IllegalArgumentException();
			} else {
				throw new IllegalArgumentException(message);
			}
		}
	}

	/**
	 * Checks if the supplied Collection does not contain null values and throws a
	 * IllegalArgumentException if it does.
	 * 
	 * @param list to be checked
	 * @param       <T> the type of the value
	 * @return The supplied value
	 * @since 1.0.0
	 */
	public static <T> Collection<T> nonNull(Collection<T> list) {
		nonNull(list.toArray(), null);
		return list;
	}

	/**
	 * Checks if the supplied Collection does not contain null values and throws a
	 * IllegalArgumentException if it does.
	 * 
	 * @param list   to be checked
	 * @param message message to be thrown in case of failure
	 * @param         <T> the type of the value
	 * @return The supplied value
	 * @since 1.0.0
	 */
	public static <T> Collection<T> nonNull(Collection<T> list, String message) {
		nonNull(list.toArray(), message);
		return list;
	}

	
}
