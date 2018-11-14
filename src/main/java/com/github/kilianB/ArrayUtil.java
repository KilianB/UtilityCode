package com.github.kilianB;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

	/**
	 * Fill a multi dimensional array with the value returned by the supplier
	 * 
	 * @param array The array to fill
	 * @param s     The supplier
	 * @param       <T> the type of the array
	 * @return the supplied array. The return value is used to allow recursive
	 *         behavior of the method and can safely be ignored by the user. The
	 *         original supplied array is identical to the returned reference. If
	 *         the supplied array is not an array null will be returned.
	 * 
	 * @since 1.2.0
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fillArrayMulti(T array, Supplier<T> s) {
		if (array != null && array.getClass().isArray()) {
			int length = Array.getLength(array);
			for (int i = 0; i < length; i++) {
				Array.set(array, i, fillArrayMulti((T) Array.get(array, i), s));
			}
		} else {
			return s.get();
		}
		return array;
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
		if (start < 0) {
			start = 0;
		}
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
	 * @param from   the index position to start searching from
	 * @param to     the last index positin to stop search at
	 * @param        <T> the type of the array
	 * @return the index position of the value if found or -1 if not present
	 * @throws ArrayIndexOutOfBoundsException if from or two are outside of the
	 *                                        array
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

	/**
	 * Find the index of the minimum value of the array.
	 * 
	 * <p>
	 * If the minimum value is present multiple times the first occurrence will be
	 * returned.
	 * 
	 * @param array the array to check
	 * @return the index of the minimum value of the array or -1 if the array is
	 *         empty
	 * @throws NullPointerException if the array is null
	 * @since 1.4.0
	 */
	public static int minimumIndex(byte[] array) {

		if (array.length == 0) {
			return -1;
		} else if (array.length == 1) {
			return 0;
		}

		int minIndex = 0;
		byte min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] < min) {
				min = array[i];
				minIndex = i;
			}
		}
		return minIndex;
	}

	/**
	 * Find the index of the minimum value of the array.
	 * 
	 * <p>
	 * If the minimum value is present multiple times the first occurrence will be
	 * returned.
	 * 
	 * @param array the array to check
	 * @return the index of the minimum value of the array or -1 if the array is
	 *         empty
	 * @throws NullPointerException if the array is null
	 * @since 1.4.0
	 */
	public static int minimumIndex(char[] array) {

		if (array.length == 0) {
			return -1;
		} else if (array.length == 1) {
			return 0;
		}

		int minIndex = 0;
		char min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] < min) {
				min = array[i];
				minIndex = i;
			}
		}
		return minIndex;
	}

	/**
	 * Find the index of the minimum value of the array.
	 * 
	 * <p>
	 * If the minimum value is present multiple times the first occurrence will be
	 * returned.
	 * 
	 * @param array the array to check
	 * @return the index of the minimum value of the array or -1 if the array is
	 *         empty
	 * @throws NullPointerException if the array is null
	 * @since 1.4.0
	 */
	public static int minimumIndex(short[] array) {

		if (array.length == 0) {
			return -1;
		} else if (array.length == 1) {
			return 0;
		}

		int minIndex = 0;
		short min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] < min) {
				min = array[i];
				minIndex = i;
			}
		}
		return minIndex;
	}

	/**
	 * Find the index of the minimum value of the array.
	 * 
	 * <p>
	 * If the minimum value is present multiple times the first occurrence will be
	 * returned.
	 * 
	 * @param array the array to check
	 * @return the index of the minimum value of the array or -1 if the array is
	 *         empty
	 * @throws NullPointerException if the array is null
	 * @since 1.4.0
	 */
	public static int minimumIndex(int[] array) {

		if (array.length == 0) {
			return -1;
		} else if (array.length == 1) {
			return 0;
		}

		int minIndex = 0;
		int min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] < min) {
				min = array[i];
				minIndex = i;
			}
		}
		return minIndex;
	}

	/**
	 * Find the index of the minimum value of the array.
	 * 
	 * <p>
	 * If the minimum value is present multiple times the first occurrence will be
	 * returned.
	 * 
	 * @param array the array to check
	 * @return the index of the minimum value of the array or -1 if the array is
	 *         empty
	 * @throws NullPointerException if the array is null
	 * @since 1.4.0
	 */
	public static int minimumIndex(long[] array) {

		if (array.length == 0) {
			return -1;
		} else if (array.length == 1) {
			return 0;
		}

		int minIndex = 0;
		long min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] < min) {
				min = array[i];
				minIndex = i;
			}
		}
		return minIndex;
	}

	/**
	 * Find the index of the minimum value of the array.
	 * 
	 * <p>
	 * If the minimum value is present multiple times the first occurrence will be
	 * returned.
	 * 
	 * @param array the array to check
	 * @return the index of the minimum value of the array or -1 if the array is
	 *         empty
	 * @throws NullPointerException if the array is null
	 * @since 1.4.0
	 */
	public static int minimumIndex(float[] array) {

		if (array.length == 0) {
			return -1;
		} else if (array.length == 1) {
			return 0;
		}

		int minIndex = 0;
		float min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] < min) {
				min = array[i];
				minIndex = i;
			}
		}
		return minIndex;
	}

	/**
	 * Find the index of the minimum value of the array.
	 * 
	 * <p>
	 * If the minimum value is present multiple times the first occurrence will be
	 * returned.
	 * 
	 * @param array the array to check
	 * @return the index of the minimum value of the array or -1 if the array is
	 *         empty
	 * @throws NullPointerException if the array is null
	 * @since 1.4.0
	 */
	public static int minimumIndex(double[] array) {

		if (array.length == 0) {
			return -1;
		} else if (array.length == 1) {
			return 0;
		}

		int minIndex = 0;
		double min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] < min) {
				min = array[i];
				minIndex = i;
			}
		}
		return minIndex;
	}

	/**
	 * Find the minimum value of the array
	 * 
	 * @param array the array to check
	 * @return the minimum value of the array
	 * @throws IndexOutOfBoundsException if the array is empty
	 * @throws NullPointerException      if the array is null
	 * @since 1.4.0
	 */
	public static byte minimum(byte[] array) {
		return array[minimumIndex(array)];
	}

	/**
	 * Find the minimum value of the array
	 * 
	 * @param array the array to check
	 * @return the minimum value of the array
	 * @throws IndexOutOfBoundsException if the array is empty
	 * @throws NullPointerException      if the array is null
	 * @since 1.4.0
	 */
	public static char minimum(char[] array) {
		return array[minimumIndex(array)];
	}

	/**
	 * Find the minimum value of the array
	 * 
	 * @param array the array to check
	 * @return the minimum value of the array
	 * @throws IndexOutOfBoundsException if the array is empty
	 * @throws NullPointerException      if the array is null
	 * @since 1.4.0
	 */
	public static short minimum(short[] array) {
		return array[minimumIndex(array)];
	}

	/**
	 * Find the minimum value of the array
	 * 
	 * @param array the array to check
	 * @return the minimum value of the array
	 * @throws IndexOutOfBoundsException if the array is empty
	 * @throws NullPointerException      if the array is null
	 * @since 1.4.0
	 */
	public static int minimum(int[] array) {
		return array[minimumIndex(array)];
	}

	/**
	 * Find the minimum value of the array
	 * 
	 * @param array the array to check
	 * @return the minimum value of the array
	 * @throws IndexOutOfBoundsException if the array is empty
	 * @throws NullPointerException      if the array is null
	 * @since 1.4.0
	 */
	public static long minimum(long[] array) {
		return array[minimumIndex(array)];
	}

	/**
	 * Find the minimum value of the array
	 * 
	 * @param array the array to check
	 * @return the minimum value of the array
	 * @throws IndexOutOfBoundsException if the array is empty
	 * @throws NullPointerException      if the array is null
	 * @since 1.4.0
	 */
	public static float minimum(float[] array) {
		return array[minimumIndex(array)];
	}

	/**
	 * Find the minimum value of the array
	 * 
	 * @param array the array to check
	 * @return the minimum value of the array
	 * @throws IndexOutOfBoundsException if the array is empty
	 * @throws NullPointerException      if the array is null
	 * @since 1.4.0
	 */
	public static double minimum(double[] array) {
		return array[minimumIndex(array)];
	}
	
	/**
	 * Find the index of the maximum value of the array.
	 * 
	 * <p>
	 * If the maximum value is present multiple times the first occurrence will be
	 * returned.
	 * 
	 * @param array the array to check
	 * @return the index of the maximum value of the array or -1 if the array is
	 *         empty
	 * @throws NullPointerException if the array is null
	 * @since 1.4.0
	 */
	public static int maximumIndex(byte[] array) {

		if (array.length == 0) {
			return -1;
		} else if (array.length == 1) {
			return 0;
		}

		int maxIndex = 0;
		byte max = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	/**
	 * Find the index of the maximum value of the array.
	 * 
	 * <p>
	 * If the maximum value is present multiple times the first occurrence will be
	 * returned.
	 * 
	 * @param array the array to check
	 * @return the index of the maximum value of the array or -1 if the array is
	 *         empty
	 * @throws NullPointerException if the array is null
	 * @since 1.4.0
	 */
	public static int maximumIndex(char[] array) {

		if (array.length == 0) {
			return -1;
		} else if (array.length == 1) {
			return 0;
		}

		int maxIndex = 0;
		char max = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}
	
	/**
	 * Find the index of the maximum value of the array.
	 * 
	 * <p>
	 * If the maximum value is present multiple times the first occurrence will be
	 * returned.
	 * 
	 * @param array the array to check
	 * @return the index of the maximum value of the array or -1 if the array is
	 *         empty
	 * @throws NullPointerException if the array is null
	 * @since 1.4.0
	 */
	public static int maximumIndex(short[] array) {

		if (array.length == 0) {
			return -1;
		} else if (array.length == 1) {
			return 0;
		}

		int maxIndex = 0;
		short max = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}
	
	/**
	 * Find the index of the maximum value of the array.
	 * 
	 * <p>
	 * If the maximum value is present multiple times the first occurrence will be
	 * returned.
	 * 
	 * @param array the array to check
	 * @return the index of the maximum value of the array or -1 if the array is
	 *         empty
	 * @throws NullPointerException if the array is null
	 * @since 1.4.0
	 */
	public static int maximumIndex(int[] array) {

		if (array.length == 0) {
			return -1;
		} else if (array.length == 1) {
			return 0;
		}

		int maxIndex = 0;
		int max = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}
	
	/**
	 * Find the index of the maximum value of the array.
	 * 
	 * <p>
	 * If the maximum value is present multiple times the first occurrence will be
	 * returned.
	 * 
	 * @param array the array to check
	 * @return the index of the maximum value of the array or -1 if the array is
	 *         empty
	 * @throws NullPointerException if the array is null
	 * @since 1.4.0
	 */
	public static int maximumIndex(long[] array) {

		if (array.length == 0) {
			return -1;
		} else if (array.length == 1) {
			return 0;
		}

		int maxIndex = 0;
		long max = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}
	
	/**
	 * Find the index of the maximum value of the array.
	 * 
	 * <p>
	 * If the maximum value is present multiple times the first occurrence will be
	 * returned.
	 * 
	 * @param array the array to check
	 * @return the index of the maximum value of the array or -1 if the array is
	 *         empty
	 * @throws NullPointerException if the array is null
	 * @since 1.4.0
	 */
	public static int maximumIndex(float[] array) {

		if (array.length == 0) {
			return -1;
		} else if (array.length == 1) {
			return 0;
		}

		int maxIndex = 0;
		float max = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}
	
	/**
	 * Find the index of the maximum value of the array.
	 * 
	 * <p>
	 * If the maximum value is present multiple times the first occurrence will be
	 * returned.
	 * 
	 * @param array the array to check
	 * @return the index of the maximum value of the array or -1 if the array is
	 *         empty
	 * @throws NullPointerException if the array is null
	 * @since 1.4.0
	 */
	public static int maximumIndex(double[] array) {

		if (array.length == 0) {
			return -1;
		} else if (array.length == 1) {
			return 0;
		}

		int maxIndex = 0;
		double max = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}
	
	/**
	 * Find the maximum value of the array
	 * 
	 * @param array the array to check
	 * @return the maximum value of the array
	 * @throws IndexOutOfBoundsException if the array is empty
	 * @throws NullPointerException      if the array is null
	 * @since 1.4.0
	 */
	public static byte maximum(byte[] array) {
		return array[maximumIndex(array)];
	}
	
	/**
	 * Find the maximum value of the array
	 * 
	 * @param array the array to check
	 * @return the maximum value of the array
	 * @throws IndexOutOfBoundsException if the array is empty
	 * @throws NullPointerException      if the array is null
	 * @since 1.4.0
	 */
	public static char maximum(char[] array) {
		return array[maximumIndex(array)];
	}
	
	/**
	 * Find the maximum value of the array
	 * 
	 * @param array the array to check
	 * @return the maximum value of the array
	 * @throws IndexOutOfBoundsException if the array is empty
	 * @throws NullPointerException      if the array is null
	 * @since 1.4.0
	 */
	public static short maximum(short[] array) {
		return array[maximumIndex(array)];
	}
	
	/**
	 * Find the maximum value of the array
	 * 
	 * @param array the array to check
	 * @return the maximum value of the array
	 * @throws IndexOutOfBoundsException if the array is empty
	 * @throws NullPointerException      if the array is null
	 * @since 1.4.0
	 */
	public static int maximum(int[] array) {
		return array[maximumIndex(array)];
	}
	
	/**
	 * Find the maximum value of the array
	 * 
	 * @param array the array to check
	 * @return the maximum value of the array
	 * @throws IndexOutOfBoundsException if the array is empty
	 * @throws NullPointerException      if the array is null
	 * @since 1.4.0
	 */
	public static long maximum(long[] array) {
		return array[maximumIndex(array)];
	}
	
	/**
	 * Find the maximum value of the array
	 * 
	 * @param array the array to check
	 * @return the maximum value of the array
	 * @throws IndexOutOfBoundsException if the array is empty
	 * @throws NullPointerException      if the array is null
	 * @since 1.4.0
	 */
	public static float maximum(float[] array) {
		return array[maximumIndex(array)];
	}
	
	/**
	 * Find the maximum value of the array
	 * 
	 * @param array the array to check
	 * @return the maximum value of the array
	 * @throws IndexOutOfBoundsException if the array is empty
	 * @throws NullPointerException      if the array is null
	 * @since 1.4.0
	 */
	public static double maximum(double[] array) {
		return array[maximumIndex(array)];
	}
	
	
	/**
	 * Add the supplied argument to every field in the array
	 * @param arr the array holding the first summand
	 * @param summand the value to add to each field
	 * @since 1.4.0
	 */
	public static void add(byte[] arr, byte summand) {
		for(int i = 0; i < arr.length; i++) {
			arr[i] += summand;
		}
	}
	
	/**
	 * Add the supplied argument to every field in the array
	 * @param arr the array holding the first summand
	 * @param summand the value to add to each field
	 * @since 1.4.0
	 */
	public static void add(char[] arr, char summand) {
		for(int i = 0; i < arr.length; i++) {
			arr[i] += summand;
		}
	}
	
	/**
	 * Add the supplied argument to every field in the array
	 * @param arr the array holding the first summand
	 * @param summand the value to add to each field
	 * @since 1.4.0
	 */
	public static void add(short[] arr, short summand) {
		for(int i = 0; i < arr.length; i++) {
			arr[i] += summand;
		}
	}
	
	/**
	 * Add the supplied argument to every field in the array
	 * @param arr the array holding the first summand
	 * @param summand the value to add to each field
	 * @since 1.4.0
	 */
	public static void add(int[] arr, int summand) {
		for(int i = 0; i < arr.length; i++) {
			arr[i] += summand;
		}
	}
	
	/**
	 * Add the supplied argument to every field in the array
	 * @param arr the array holding the first summand
	 * @param summand the value to add to each field
	 * @since 1.4.0
	 */
	public static void add(long[] arr, long summand) {
		for(int i = 0; i < arr.length; i++) {
			arr[i] += summand;
		}
	}
	
	/**
	 * Add the supplied argument to every field in the array
	 * @param arr the array holding the first summand
	 * @param summand the value to add to each field
	 * @since 1.4.0
	 */
	public static void add(float[] arr, float summand) {
		for(int i = 0; i < arr.length; i++) {
			arr[i] += summand;
		}
	}
	
	/**
	 * Add the supplied argument to every field in the array
	 * @param arr the array holding the first summand
	 * @param summand the value to add to each field
	 * @since 1.4.0
	 */
	public static void add(double[] arr, double summand) {
		for(int i = 0; i < arr.length; i++) {
			arr[i] += summand;
		}
	}

	/**
	 * Calculate the difference for each field in the array
	 * @param arrMinuend the array holding the minuend
	 * @param subtrahend the value to subtracted from each field
	 * @since 1.4.0
	 */
	public static void subtract(byte[] arrMinuend, byte subtrahend) {
		for(int i = 0; i < arrMinuend.length; i++) {
			arrMinuend[i] -= subtrahend;
		}
	}
	
	/**
	 * Calculate the difference for each field in the array
	 * @param arrMinuend the array holding the minuend
	 * @param subtrahend the value to subtracted from each field
	 * @since 1.4.0
	 */
	public static void subtract(char[] arrMinuend, char subtrahend) {
		for(int i = 0; i < arrMinuend.length; i++) {
			arrMinuend[i] -= subtrahend;
		}
	}
	
	/**
	 * Calculate the difference for each field in the array
	 * @param arrMinuend the array holding the minuend
	 * @param subtrahend the value to subtracted from each field
	 * @since 1.4.0
	 */
	public static void subtract(short[] arrMinuend, short subtrahend) {
		for(int i = 0; i < arrMinuend.length; i++) {
			arrMinuend[i] -= subtrahend;
		}
	}
	
	/**
	 * Calculate the difference for each field in the array
	 * @param arrMinuend the array holding the minuend
	 * @param subtrahend the value to subtracted from each field
	 * @since 1.4.0
	 */
	public static void subtract(int[] arrMinuend, int subtrahend) {
		for(int i = 0; i < arrMinuend.length; i++) {
			arrMinuend[i] -= subtrahend;
		}
	}
	
	/**
	 * Calculate the difference for each field in the array
	 * @param arrMinuend the array holding the minuend
	 * @param subtrahend the value to subtracted from each field
	 * @since 1.4.0
	 */
	public static void subtract(long[] arrMinuend, long subtrahend) {
		for(int i = 0; i < arrMinuend.length; i++) {
			arrMinuend[i] -= subtrahend;
		}
	}
	
	/**
	 * Calculate the difference for each field in the array
	 * @param arrMinuend the array holding the minuend
	 * @param subtrahend the value to subtracted from each field
	 * @since 1.4.0
	 */
	public static void subtract(float[] arrMinuend, float subtrahend) {
		for(int i = 0; i < arrMinuend.length; i++) {
			arrMinuend[i] -= subtrahend;
		}
	}
	
	/**
	 * Calculate the difference for each field in the array
	 * @param arrMinuend the array holding the minuend
	 * @param subtrahend the value to subtracted from each field
	 * @since 1.4.0
	 */
	public static void subtract(double[] arrMinuend, double subtrahend) {
		for(int i = 0; i < arrMinuend.length; i++) {
			arrMinuend[i] -= subtrahend;
		}
	}
	
	/**
	 * Calculate the product for each field in the array
	 * @param arr the array holding the first factor
	 * @param factor the value to multiply each field by
	 * @since 1.4.0
	 */
	public static void multiply(byte[] arr, byte factor) {
		for(int i = 0; i < arr.length; i++) {
			arr[i] *= factor;
		}
	}
	
	/**
	 * Calculate the product for each field in the array
	 * @param arr the array holding the first factor
	 * @param factor the value to multiply each field by
	 * @since 1.4.0
	 */
	public static void multiply(char[] arr, char factor) {
		for(int i = 0; i < arr.length; i++) {
			arr[i] *= factor;
		}
	}
	
	/**
	 * Calculate the product for each field in the array
	 * @param arr the array holding the first factor
	 * @param factor the value to multiply each field by
	 * @since 1.4.0
	 */
	public static void multiply(short[] arr, short factor) {
		for(int i = 0; i < arr.length; i++) {
			arr[i] *= factor;
		}
	}
	
	/**
	 * Calculate the product for each field in the array
	 * @param arr the array holding the first factor
	 * @param factor the value to multiply each field by
	 * @since 1.4.0
	 */
	public static void multiply(int[] arr, int factor) {
		for(int i = 0; i < arr.length; i++) {
			arr[i] *= factor;
		}
	}
	
	
	/**
	 * Calculate the product for each field in the array
	 * @param arr the array holding the first factor
	 * @param factor the value to multiply each field by
	 * @since 1.4.0
	 */
	public static void multiply(long[] arr, long factor) {
		for(int i = 0; i < arr.length; i++) {
			arr[i] *= factor;
		}
	}
	
	/**
	 * Calculate the product for each field in the array
	 * @param arr the array holding the first factor
	 * @param factor the value to multiply each field by
	 * @since 1.4.0
	 */
	public static void multiply(float[] arr, float factor) {
		for(int i = 0; i < arr.length; i++) {
			arr[i] *= factor;
		}
	}
	
	/**
	 * Calculate the product for each field in the array
	 * @param arr the array holding the first factor
	 * @param factor the value to multiply each field by
	 * @since 1.4.0
	 */
	public static void multiply(double[] arr, double factor) {
		for(int i = 0; i < arr.length; i++) {
			arr[i] *= factor;
		}
	}
	
	/**
	 * Calculate the quotient for each field in the array
	 * @param arrDividend the array holding the dividend
	 * @param divisor the divisor each field is divided by
	 * @since 1.4.0
	 */
	public static void divide(byte[] arrDividend, byte divisor) {
		for(int i = 0; i < arrDividend.length; i++) {
			arrDividend[i] /= divisor;
		}
	}
	
	/**
	 * Calculate the quotient for each field in the array
	 * @param arrDividend the array holding the dividend
	 * @param divisor the divisor each field is divided by
	 * @since 1.4.0
	 */
	public static void divide(char[] arrDividend, char divisor) {
		for(int i = 0; i < arrDividend.length; i++) {
			arrDividend[i] /= divisor;
		}
	}
	
	/**
	 * Calculate the quotient for each field in the array
	 * @param arrDividend the array holding the dividend
	 * @param divisor the divisor each field is divided by
	 * @since 1.4.0
	 */
	public static void divide(short[] arrDividend, short divisor) {
		for(int i = 0; i < arrDividend.length; i++) {
			arrDividend[i] /= divisor;
		}
	}
	
	/**
	 * Calculate the quotient for each field in the array
	 * @param arrDividend the array holding the dividend
	 * @param divisor the divisor each field is divided by
	 * @since 1.4.0
	 */
	public static void divide(int[] arrDividend, int divisor) {
		for(int i = 0; i < arrDividend.length; i++) {
			arrDividend[i] /= divisor;
		}
	}
	
	/**
	 * Calculate the quotient for each field in the array
	 * @param arrDividend the array holding the dividend
	 * @param divisor the divisor each field is divided by
	 * @since 1.4.0
	 */
	public static void divide(long[] arrDividend,long divisor) {
		for(int i = 0; i < arrDividend.length; i++) {
			arrDividend[i] /= divisor;
		}
	}
	
	/**
	 * Calculate the quotient for each field in the array
	 * @param arrDividend the array holding the dividend
	 * @param divisor the divisor each field is divided by
	 * @since 1.4.0
	 */
	public static void divide(float[] arrDividend, float divisor) {
		for(int i = 0; i < arrDividend.length; i++) {
			arrDividend[i] /= divisor;
		}
	}
	
	/**
	 * Calculate the quotient for each field in the array
	 * @param arrDividend the array holding the dividend
	 * @param divisor the divisor each field is divided by
	 * @since 1.4.0
	 */
	public static void divide(double[] arrDividend, double divisor) {
		for(int i = 0; i < arrDividend.length; i++) {
			arrDividend[i] /= divisor;
		}
	}
}
