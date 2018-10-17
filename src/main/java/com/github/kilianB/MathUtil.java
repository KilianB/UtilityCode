package com.github.kilianB;

/**
 * @author Kilian
 *
 */
public class MathUtil {

	/**
	 * Clamp a number between its lower and upper bound. If x {@literal>} upper bound return
	 * upper bound. If x {@literal<} lower bound return lower bound
	 * 
	 * @param value      the input value
	 * @param lowerBound the lower bound
	 * @param upperBound the upper bound
	 * @param <T> The type of the input and return value
	 * @return the original value if it's between bounds or the bound
	 * @since 1.0.0
	 */
	public static <T extends Number & Comparable<T>> T clampNumber(T value, T lowerBound, T upperBound) {
		if (value.compareTo(lowerBound) <= 0) {
			return lowerBound;
		} else if (value.compareTo(upperBound) >= 0) {
			return upperBound;
		}
		return value;
	}

	/**
	 * Find the closest value to a number which can be divided by the divisor.
	 * 
	 * <pre>
	 * if  dividend%divisor == 0 return dividend
	 * if  dividend%divisor != 0 return the value closest
	 *     to dividend which fullfiles the condition
	 * </pre>
	 * 
	 * If two numbers are exactly the same distance away one of them
	 * will returned.
	 * 
	 * @param dividend the dividend
	 * @param divisor  the divisor
	 * @return the nearest number to dividend which can be divided by divisor
	 * @throws java.lang.ArithmeticException if divisor is zero
	 * 
	 * @since 1.0.0
	 */
	public static long findClosestDivisibleInteger(int dividend, int divisor) {
		int quot = dividend / divisor;

		int n1 = divisor * quot;

		int n2 = (dividend * divisor) > 0 ? (divisor * (quot + 1)) : (divisor * (quot - 1));

		if (Math.abs(dividend - n1) < Math.abs(dividend - n2))
			return n1;

		return n2;
	}

	/**
	 * Compare two doubles. Necessary due to non exact floating point arithmetics
	 * 
	 * @param needle  the needle
	 * @param target  the target
	 * @param epsilon the amount the values may differ to still consider a match
	 * @return true if numbers are considered equal, false otherwise
	 * @since 1.0.0
	 */
	public static boolean isDoubleEquals(double needle, double target, double epsilon) {
		// We could use machine precision e.g. Math.ulp(d)
		return Math.abs(needle - target) <= epsilon;
	}

	/**
	 * Return the fractional part of the number
	 * 
	 * @param d a double
	 * @return the fractional part of the number. If the base number is negative the returned
	 * fraction will also be negative.
	 * @since 1.0.0
	 */
	public static double getFractionalPart(double d) {
		return d - (int) d;
	}

	/**
	 * 
	 * @param gaussian A gaussian with std 1 and mean 0
	 * @param newStd   new standard deviation
	 * @param newMean  new mean
	 * @return a value fitted to the new mean and std
	 * @since 1.0.0
	 */
	public static double fitGaussian(double gaussian, double newStd, double newMean) {
		return gaussian * newStd + newMean;
	}

}
