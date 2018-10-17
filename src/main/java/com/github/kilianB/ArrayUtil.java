package com.github.kilianB;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author Kilian
 *
 */
public class ArrayUtil {

	/**
	 * Return the string representation of an array containing floating point
	 * numbers with fixed decimal places.The string representation consists of a
	 * list of the array's elements,enclosed in square brackets ("[]"). Adjacent
	 * elements are separated by the characters ", " (a comma followed by a space).
	 * Elements are converted to strings as by String.valueOf(double). Returns
	 * "null" if a is null.
	 * 
	 * @param array         The array content to convert to a string
	 * @param decimalPlaces the number of fractional numbers shown for each entry
	 * @return a string representation of the double array
	 * @since 1.0.0
	 */
	public static String toString(double[] array, int decimalPlaces) {
		return toString((Object) array, decimalPlaces);
	}

	/**
	 * Return the string representation of an array containing floating point
	 * numbers with fixed decimal places. The string representation consists of a
	 * list of the array's elements,enclosed in square brackets ("[]"). Adjacent
	 * elements are separated by the characters ", " (a comma followed by a space).
	 * Elements are converted to strings as by String.valueOf(float). Returns "null"
	 * if a is null.
	 * 
	 * @param array         The array content to convert to a string
	 * @param decimalPlaces the number of fractional numbers shown for each entry
	 * @return a string representation of the float array
	 * @since 1.0.0
	 */
	public static String toString(float[] array, int decimalPlaces) {
		return toString((Object) array, decimalPlaces);
	}

	/**
	 * Return the string representation of an array containing floating point
	 * numbers with fixed decimal places.
	 * 
	 * @param boxedFloats
	 * @param decimalPlaces
	 * @return the string representation of the array
	 * @since 1.0.0
	 */
	private static String toString(Object boxedFloats, int decimalPlaces) {
		String format = "%." + decimalPlaces + "f";

		if (boxedFloats == null)
			return "null";
		int iMax = Array.getLength(boxedFloats) - 1;
		if (iMax == -1)
			return "[]";

		StringBuilder b = new StringBuilder();
		b.append('[');
		for (int i = 0;; i++) {

			b.append(String.format(format, Array.get(boxedFloats, i)));
			if (i == iMax)
				return b.append(']').toString();
			b.append(", ");
		}
	}

	@SuppressWarnings("unused")
	@Deprecated // TODO extend for further usage. Currently no nested array
	private static String toDeepString(Object boxedFloats, int decimalPlaces) {
		String format = "%." + decimalPlaces + "f";

		if (boxedFloats == null)
			return "null";
		int iMax = Array.getLength(boxedFloats) - 1;
		if (iMax == -1)
			return "[]";

		StringBuilder b = new StringBuilder();
		b.append('[');
		for (int i = 0;; i++) {

			Object elem = Array.get(boxedFloats, i);

			if (elem.getClass().isArray()) {
				b.append(toDeepString(elem, decimalPlaces));
			} else {
				b.append(String.format(format, elem));
			}

			if (i == iMax)
				return b.append(']').toString();
			b.append(", ");
		}
	}

	/**
	 * <i>Deep copy</i> nested arrays. A shallow copy of each array contained in
	 * this array will be created allowing to alter the values without affecting the
	 * original data structure. Be aware that the individual components are only
	 * copied and still point to the same object.
	 * 
	 * <p>
	 * If a true deep clone is required take a look at
	 * {@link #deepArrayCopyClone(Object[])} instead.
	 * 
	 * @param array The array to clone
	 * @param       <T> the type of the array
	 * @return an new array with all nested arrays being replaced by copies.
	 * @since 1.0.0
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] deepArrayCopy(T[] array) {

		if (0 >= array.length)
			return array;

		return (T[]) deepCopyOf(array, Array.newInstance(array[0].getClass(), array.length), 0);
	}

	/**
	 * Deep copies the array structures as well as attempts to call
	 * {@link java.lang.Object#clone} if the element implements the the cloneable
	 * interface. Even if cloneable is implemented no guarantee is made that the
	 * returned element is in fact a deep clone of the base object. The class
	 * specific clone implementation has to be looked at individually.
	 * 
	 * Untested
	 * 
	 * @param array The array to clone
	 * @param       <T> the type of the array
	 * @return a truly deep cloned array
	 * @throws Exception if an exception occurs during cloning of individual objects
	 * @since 1.0.0
	 */
	@Deprecated
	public static <T> T[] deepArrayCopyClone(T[] array) throws Exception {
		if (0 >= array.length)
			return array;

		return (T[]) deepCloneCopyOf(array, Array.newInstance(array[0].getClass(), array.length), 0);
	}

	private static Object deepCopyOf(Object array, Object copiedArray, int index) {

		if (index >= Array.getLength(array))
			return copiedArray;

		Object element = Array.get(array, index);

		// Fix 17.10 copy null elements.
		if (element != null && element.getClass().isArray()) {

			Array.set(copiedArray, index, deepCopyOf(element,
					Array.newInstance(element.getClass().getComponentType(), Array.getLength(element)), 0));

		} else {

			Array.set(copiedArray, index, element);
		}

		return deepCopyOf(array, copiedArray, ++index);
	}

	@Deprecated
	private static Object deepCloneCopyOf(Object array, Object copiedArray, int index) throws NoSuchMethodException,
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		if (index >= Array.getLength(array))
			return copiedArray;

		Object element = Array.get(array, index);

		if (element == null) {
			Array.set(copiedArray, index, element);
		} else {
			if (element.getClass().isArray()) {
				Array.set(copiedArray, index, deepCopyOf(element,
						Array.newInstance(element.getClass().getComponentType(), Array.getLength(element)), 0));
			} else {
				if (element instanceof Cloneable) {
					// We assume the clone method to be overwritten
					Method clone = element.getClass().getMethod("clone");
					Object cloned = clone.invoke(element);
					Array.set(copiedArray, index, cloned);
				} else {
					Array.set(copiedArray, index, element);
				}
			}
		}

		return deepCopyOf(array, copiedArray, ++index);
	}

	/**
	 * Check if all values in the array are not null. This operation supports nested
	 * arrays
	 * 
	 * @param array to check
	 * @return true if all elements in the array are not null
	 * @since 1.0.0
	 */
	public static boolean deepAllNotNull(Object[] array) {
		for (Object o : array) {
			if (o == null)
				return false;
			if (o.getClass().isArray() && !deepAllNotNull((Object[]) o)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Check if all values in the array are not null
	 * 
	 * @param array to check
	 * @param       <T> the type of the array
	 * @return true if all elements in the array are not null
	 * @since 1.0.0
	 */
	public static <T> boolean allNotNull(T[] array) {
		for (T t : array) {
			if (t == null)
				return false;
		}
		return true;
	}

	/**
	 * Check if all values in the array are null
	 * 
	 * @param array to check
	 * @param       <T> the type of the array
	 * @return true if all elements in the array are null
	 * @since 1.0.0
	 */
	public static <T> boolean allNull(T[] array) {
		for (T t : array) {
			if (t != null)
				return false;
		}
		return true;
	}

	/**
	 * Map 2 dimensional coordinates to a one dimensional array
	 * 
	 * @param x     The first coordinate
	 * @param y     The second coordinate
	 * @param width the width of the 2 dim array
	 * @return a mapped integer.
	 * @since 1.0.0
	 */
	public static int twoDimtoOneDim(int x, int y, int width) {
		return x * width + y;
	}

	/**
	 * Fill the entire array with the value returned by the supplier.
	 * 
	 * @param array The array to fill
	 * @param s     The supplier used
	 * @param       <T> The type of the array
	 * @since 1.0.0
	 */
	public static <T> void fillArray(T[] array, Supplier<T> s) {
		for (int i = 0; i < array.length; i++) {
			array[i] = s.get();
		}
	}

	// primitive instances can't be auto unboxed

	/**
	 * Fill the entire array with the value returned by the supplier.
	 * 
	 * @param array The array to fill
	 * @param s     The supplier used
	 * @since 1.0.0
	 */
	public static void fillArray(boolean[] array, Supplier<Boolean> s) {
		for (int i = 0; i < array.length; i++) {
			array[i] = s.get();
		}
	}

	/**
	 * Fill the entire array with the value returned by the supplier.
	 * 
	 * @param array The array to fill
	 * @param s     The supplier used
	 * @since 1.0.0
	 */
	public static void fillArray(byte[] array, Supplier<Byte> s) {
		for (int i = 0; i < array.length; i++) {
			array[i] = s.get();
		}
	}


	/**
	 * Fill the entire array with the value returned by the supplier.
	 * 
	 * @param array The array to fill
	 * @param s     The supplier used
	 * @since 1.0.0
	 */
	public static void fillArray(char[] array, Supplier<Character> s) {
		for (int i = 0; i < array.length; i++) {
			array[i] = s.get();
		}
	}

	/**
	 * Fill the entire array with the value returned by the supplier.
	 * 
	 * @param array The array to fill
	 * @param s     The supplier used
	 * @since 1.0.0
	 */
	public static void fillArray(short[] array, Supplier<Short> s) {
		for (int i = 0; i < array.length; i++) {
			array[i] = s.get();
		}
	}

	/**
	 * Fill the entire array with the value returned by the supplier.
	 * 
	 * @param array The array to fill
	 * @param s     The supplier used
	 * @since 1.0.0
	 */
	public static void fillArray(float[] array, Supplier<Float> s) {
		for (int i = 0; i < array.length; i++) {
			array[i] = s.get();
		}
	}

	/**
	 * Fill the entire array with the value returned by the supplier.
	 * 
	 * @param array The array to fill
	 * @param s     The supplier used
	 * @since 1.0.0
	 */
	public static void fillArray(int[] array, Supplier<Integer> s) {
		for (int i = 0; i < array.length; i++) {
			array[i] = s.get();
		}
	}

	/**
	 * Fill the entire array with the value returned by the supplier.
	 * 
	 * @param array The array to fill
	 * @param s     The supplier used
	 * @since 1.0.0
	 */
	public static void fillArray(long[] array, Supplier<Long> s) {
		for (int i = 0; i < array.length; i++) {
			array[i] = s.get();
		}
	}

	/**
	 * Fill the entire array with the value returned by the supplier.
	 * 
	 * @param array The array to fill
	 * @param s     The supplier used
	 * @since 1.0.0
	 */
	public static void fillArray(double[] array, Supplier<Double> s) {
		for (int i = 0; i < array.length; i++) {
			array[i] = s.get();
		}
	}

	// Search

	/**
	 * Search the specified array for the specified value. This operation works on
	 * unsorted array but has a O(N) worst case time complexity. For efficient
	 * searching consider {@link java.util.Arrays#binarySearch}.
	 * 
	 * <p>
	 * If the same array gets searched consistently you may also consider creating a
	 * hash index to bring down the search to O(1).
	 * 
	 * @param array  the array to be searched
	 * @param needle the value to search for
	 * @param start  the index position to start searching from
	 * @param stop   the last index position to stop search at
	 * @param        <T> the type of the array
	 * @return the index position of the value if found or -1 if not present
	 * @since 1.0.0
	 */
	public static <T> int linearSearch(T[] array, T needle, int start, int stop) {
		int maxIndex = Math.min(array.length, stop);
		for (int i = start; i < maxIndex; i++) {
			if (array[i].equals(needle)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Search the specified array for the specified value. This operation works on
	 * unsorted array but has a O(N) worst case time complexity. For efficient
	 * searching consider {@link java.util.Arrays#binarySearch}.
	 * 
	 * <p>
	 * If the same array gets searched consistently you may also consider creating a
	 * hash index to bring down the search to O(1).
	 * 
	 * 
	 * @param array  the array to be searched
	 * @param needle the value to search for
	 * @param        <T> the type of the array
	 * @return the index position of the value if found or -1 if not present
	 * @since 1.0.0
	 */
	public static <T> int linearSearch(T[] array, T needle) {
		return linearSearch(array, needle, 0, array.length);
	}

	/**
	 * Search the specified array for the specified value. This operation tries to
	 * minimize the number of comparisons. The array is search from the beginning
	 * and end simultaneously resulting in worst case performance if the searched
	 * value is exactly in the middle. For efficient searching consider
	 * {@link java.util.Arrays#binarySearch}.
	 * 
	 * <p>
	 * If the same array gets searched consistently you may also consider creating a
	 * hash index to bring down the search to O(1).
	 * 
	 * @param array  the array to be searched
	 * @param needle the value to search for
	 * @param from  the index position to start searching from
	 * @param to   the last index positin to stop search at
	 * @param        <T> the type of the array
	 * @return the index position of the value if found or -1 if not present
	 * @since 1.0.0
	 */
	public static <T> int frontBackSearch(T[] array, T needle, int from, int to) {
		to--;
		while (from <= to) {
			if (array[from].equals(needle)) {
				return from;
			}
			if (array[to].equals(needle)) {
				return to;
			}
			from++;
			to--;
		}
		return -1;
	}

	/**
	 * Search the specified array for the specified value. This operation tries to
	 * minimize the number of comparisons. The array is search from the beginning
	 * and end simultaneously resulting in worst case performance if the searched
	 * value is exactly in the middle. For efficient searching consider
	 * {@link java.util.Arrays#binarySearch}.
	 * 
	 * <p>
	 * If the same array gets searched consistently you may also consider creating a
	 * hash index to bring down the search to O(1).
	 * 
	 * @param array  the array to be searched
	 * @param needle the value to search for
	 * @param        <T> the type of the array
	 * @return the index position of the value if found or -1 if not present
	 * @since 1.0.0
	 */
	public static <T> int frontBackSearch(T[] array, T needle) {
		return (frontBackSearch(array, needle, 0, array.length));
	}

}
