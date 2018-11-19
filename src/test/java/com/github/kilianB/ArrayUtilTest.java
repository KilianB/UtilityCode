package com.github.kilianB;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.geom.Point2D;
import java.util.Arrays;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.github.kilianB.mutable.MutableInteger;

/**
 * @author Kilian
 *
 */
class ArrayUtilTest {

	@Test
	void fillArray() {
		Integer fill = Integer.MIN_VALUE;
		Integer[] intArr = new Integer[10];
		ArrayUtil.fillArray(intArr, () -> {
			return fill;
		});
		for (Integer i : intArr) {
			assertEquals(fill, i);
		}
	}

	@Test
	void fillArrayBool() {
		boolean value = true;
		boolean[] array = new boolean[10];
		ArrayUtil.fillArray(array, () -> {
			return value;
		});
		for (boolean i : array) {
			assertEquals(value, i);
		}
	}

	@Test
	void fillArrayByte() {
		byte value = 1;
		byte[] array = new byte[10];
		ArrayUtil.fillArray(array, () -> {
			return value;
		});
		for (byte i : array) {
			assertEquals(value, i);
		}
	}

	@Test
	void fillArrayChar() {
		char value = 'c';
		char[] array = new char[10];
		ArrayUtil.fillArray(array, () -> {
			return value;
		});
		for (char i : array) {
			assertEquals(value, i);
		}
	}

	@Test
	void fillArrayShort() {
		short value = 1;
		short[] array = new short[10];
		ArrayUtil.fillArray(array, () -> {
			return value;
		});
		for (short i : array) {
			assertEquals(value, i);
		}
	}

	@Test
	void fillArrayFloat() {
		float value = 1f;
		float[] array = new float[10];
		ArrayUtil.fillArray(array, () -> {
			return value;
		});
		for (float i : array) {
			assertEquals(value, i);
		}
	}

	@Test
	void fillArrayDouble() {
		double value = 1d;
		double[] array = new double[10];
		ArrayUtil.fillArray(array, () -> {
			return value;
		});
		for (double i : array) {
			assertEquals(value, i);
		}
	}

	@Test
	void fillArrayInt() {
		int value = 1;
		int[] array = new int[10];
		ArrayUtil.fillArray(array, () -> {
			return value;
		});
		for (int i : array) {
			assertEquals(value, i);
		}
	}

	@Test
	void fillArrayLong() {
		long value = 1l;
		long[] array = new long[10];
		ArrayUtil.fillArray(array, () -> {
			return value;
		});
		for (long i : array) {
			assertEquals(value, i);
		}
	}

	@Test
	void fillArrayMulti() {
		Integer value = Integer.MIN_VALUE;
		Integer[][] intArr = new Integer[10][10];
		ArrayUtil.fillArrayMulti(intArr, () -> {
			return value;
		});
		for (int i = 0; i < intArr.length; i++) {
			for (int j = 0; j < intArr[0].length; j++) {
				assertEquals(value, intArr[i][j]);
			}
		}
	}

	@Test
	void fillArrayMultiOneElement() {
		Integer value = Integer.MIN_VALUE;
		Integer intArr = null;
		ArrayUtil.fillArrayMulti(intArr, () -> {
			return value;
		});
		assertEquals(null, intArr);
	}

	@Test
	void fillArrayMultiThreeDims() {
		Integer value = Integer.MIN_VALUE;
		Integer[][][] intArr = new Integer[9][7][8];
		ArrayUtil.fillArrayMulti(intArr, () -> {
			return value;
		});
		for (int i = 0; i < intArr.length; i++) {
			for (int j = 0; j < intArr[0].length; j++) {
				for (int m = 0; m < intArr[0][0].length; m++) {
					assertEquals(value, intArr[i][j][m]);
				}
			}
		}
	}

	@Test
	void deepArrayCopyShallowOuter() {
		Integer[] intArr = new Integer[10];
		for (int i = 0; i < 10; i++) {
			intArr[i] = Integer.valueOf(i);
		}
		Integer[] clone = ArrayUtil.deepArrayCopy(intArr);
		assertTrue(clone != intArr);
	}

	@Test
	void deepArrayCopyElements() {
		Integer[] intArr = new Integer[10];
		for (int i = 0; i < 10; i++) {
			intArr[i] = Integer.valueOf(i);
		}
		Integer[] clone = ArrayUtil.deepArrayCopy(intArr);
		assertTrue(Arrays.deepEquals(intArr, clone));
	}

	// Now get to the nested part

	@Test
	void deepArrayCopyElementsMultiDim() {
		Integer[][] intArr = new Integer[10][];
		for (int i = 0; i < 10; i++) {
			intArr[i] = new Integer[4];
			for (int j = 0; j < 4; j++) {
				intArr[i][j] = 1;
			}
		}
		Integer[][] clone = ArrayUtil.deepArrayCopy(intArr);
		assertTrue(Arrays.deepEquals(intArr, clone));
	}

	@Test
	void deepArrayCopyElementsMultiDimNullValues() {
		Integer[][] intArr = new Integer[10][];
		for (int i = 0; i < 10; i++) {
			intArr[i] = new Integer[4];
		}
		Integer[][] clone = ArrayUtil.deepArrayCopy(intArr);
		assertTrue(Arrays.deepEquals(intArr, clone));
	}

	@Test
	void deepArrayCopyElementsMultiDeepReference() {
		Point2D[][] intArr = new Point2D[10][];
		for (int i = 0; i < 10; i++) {
			intArr[i] = new Point2D[4];
		}
		Point2D[][] clone = ArrayUtil.deepArrayCopy(intArr);
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 4; j++) {
				if (intArr[i][j] != clone[i][j]) {
					fail("References of base not equals");
				}
			}
		}
	}

	@Test
	@Disabled
	void deepArrayCopyElementNested() {
		Object[] array = new Object[10];
		for (int i = 0; i < 10; i++) {
			if (i % 2 == 0) {
				array[i] = new javafx.geometry.Point2D(0, 0);
			} else {
				Object[] o = new Object[4];
				array[i] = o;
				for (int j = 0; j < 4; j++) {
					o[j] = new javafx.geometry.Point2D(0, 0);
				}
			}
		}
		Object[] clone = ArrayUtil.deepArrayCopy(array);
		Arrays.deepEquals(array, clone);
	}

	// Search
	@Test
	void linearSearch() {
		Integer[] arr = new Integer[10];
		MutableInteger i = new MutableInteger(0);
		ArrayUtil.fillArray(arr, () -> {
			return i.getAndIncrement();
		});
		assertEquals(3, ArrayUtil.linearSearch(arr, 3));
	}

	@Test
	void linearSearchNotInArray() {
		Integer[] arr = new Integer[10];
		MutableInteger i = new MutableInteger(0);
		ArrayUtil.fillArray(arr, () -> {
			return i.getAndIncrement();
		});
		assertEquals(-1, ArrayUtil.linearSearch(arr, 10));
	}

	@Test
	void linearSearchRange() {
		Integer[] arr = new Integer[10];
		MutableInteger i = new MutableInteger(0);
		ArrayUtil.fillArray(arr, () -> {
			return i.getAndIncrement();
		});
		assertEquals(3, ArrayUtil.linearSearch(arr, 3, 1, 4));
	}

	@Test
	void linearSearchRangeNotInArray() {
		Integer[] arr = new Integer[10];
		MutableInteger i = new MutableInteger(0);
		ArrayUtil.fillArray(arr, () -> {
			return i.getAndIncrement();
		});
		assertEquals(-1, ArrayUtil.linearSearch(arr, 10, 1, 4));
	}

	@Test
	void linearSearchResultInLower() {
		Integer[] arr = new Integer[10];
		MutableInteger i = new MutableInteger(0);
		ArrayUtil.fillArray(arr, () -> {
			return i.getAndIncrement();
		});
		assertEquals(-1, ArrayUtil.linearSearch(arr, 3, 4, 6));
	}

	@Test
	void linearSearchRangeInUpper() {
		Integer[] arr = new Integer[10];
		MutableInteger i = new MutableInteger(0);
		ArrayUtil.fillArray(arr, () -> {
			return i.getAndIncrement();
		});
		assertEquals(-1, ArrayUtil.linearSearch(arr, 5, 1, 4));
	}

	@Test
	void frontBackSearch() {
		Integer[] arr = new Integer[10];
		MutableInteger i = new MutableInteger(0);
		ArrayUtil.fillArray(arr, () -> {
			return i.getAndIncrement();
		});
		assertEquals(3, ArrayUtil.frontBackSearch(arr, 3));
	}

	@Test
	void frontBackSearchNotInArray() {
		Integer[] arr = new Integer[10];
		MutableInteger i = new MutableInteger(0);
		ArrayUtil.fillArray(arr, () -> {
			return i.getAndIncrement();
		});
		assertEquals(-1, ArrayUtil.frontBackSearch(arr, 10));
	}

	@Test
	void frontBackSearchRange() {
		Integer[] arr = new Integer[10];
		MutableInteger i = new MutableInteger(0);
		ArrayUtil.fillArray(arr, () -> {
			return i.getAndIncrement();
		});
		assertEquals(3, ArrayUtil.frontBackSearch(arr, 3, 1, 4));
	}

	@Test
	void frontBackSearchRangeNotInArray() {
		Integer[] arr = new Integer[10];
		MutableInteger i = new MutableInteger(0);
		ArrayUtil.fillArray(arr, () -> {
			return i.getAndIncrement();
		});
		assertEquals(-1, ArrayUtil.frontBackSearch(arr, 10, 1, 4));
	}

	@Test
	void frontBackSearchResultInLower() {
		Integer[] arr = new Integer[10];
		MutableInteger i = new MutableInteger(0);
		ArrayUtil.fillArray(arr, () -> {
			return i.getAndIncrement();
		});
		assertEquals(-1, ArrayUtil.frontBackSearch(arr, 3, 4, 6));
	}

	@Test
	void frontBackSearchRangeInUpper() {
		Integer[] arr = new Integer[10];
		MutableInteger i = new MutableInteger(0);
		ArrayUtil.fillArray(arr, () -> {
			return i.getAndIncrement();
		});
		assertEquals(-1, ArrayUtil.frontBackSearch(arr, 5, 1, 4));
	}

	@Nested
	class ScalarArrayMath {

		@Nested
		class Add {
			@Test
			void byteAdd() {
				byte[] arr = new byte[10];
				byte[] test = new byte[10];

				ArrayUtil.add(arr, (byte) 2);
				ArrayUtil.fillArray(test, () -> {
					return (byte) 2;
				});
				assertArrayEquals(test, arr);
			}

			@Test
			void charAdd() {
				char[] arr = new char[10];
				char[] test = new char[10];

				ArrayUtil.add(arr, (char) 2);
				ArrayUtil.fillArray(test, () -> {
					return (char) 2;
				});
				assertArrayEquals(test, arr);
			}

			@Test
			void shortAdd() {
				short[] arr = new short[10];
				short[] test = new short[10];

				ArrayUtil.add(arr, (short) 2);
				ArrayUtil.fillArray(test, () -> {
					return (short) 2;
				});
				assertArrayEquals(test, arr);
			}

			@Test
			void intAdd() {
				int[] arr = new int[10];
				int[] test = new int[10];

				ArrayUtil.add(arr, 2);
				ArrayUtil.fillArray(test, () -> {
					return 2;
				});
				assertArrayEquals(test, arr);
			}

			@Test
			void longAdd() {
				long[] arr = new long[10];
				long[] test = new long[10];

				ArrayUtil.add(arr, 2);
				ArrayUtil.fillArray(test, () -> {
					return 2l;
				});
				assertArrayEquals(test, arr);
			}

			@Test
			void floatAdd() {
				float[] arr = new float[10];
				float[] test = new float[10];

				ArrayUtil.add(arr, 2f);
				ArrayUtil.fillArray(test, () -> {
					return 2f;
				});
				assertArrayEquals(test, arr);
			}

			@Test
			void doubleAdd() {
				double[] arr = new double[10];
				double[] test = new double[10];

				ArrayUtil.add(arr, 2);
				ArrayUtil.fillArray(test, () -> {
					return 2d;
				});
				assertArrayEquals(test, arr);
			}
		}

		class Subtract {
			@Test
			void byteSub() {
				byte[] arr = new byte[10];
				byte[] test = new byte[10];

				ArrayUtil.fillArray(arr, () -> {
					return (byte) 4;
				});
				ArrayUtil.subtract(arr, (byte) 2);
				ArrayUtil.fillArray(test, () -> {
					return (byte) 2;
				});
				assertArrayEquals(test, arr);
			}

			@Test
			void charSub() {
				char[] arr = new char[10];
				char[] test = new char[10];

				ArrayUtil.fillArray(arr, () -> {
					return (char) 4;
				});
				ArrayUtil.subtract(arr, (char) 2);
				ArrayUtil.fillArray(test, () -> {
					return (char) 2;
				});
				assertArrayEquals(test, arr);
			}

			@Test
			void shortSub() {
				short[] arr = new short[10];
				short[] test = new short[10];

				ArrayUtil.fillArray(arr, () -> {
					return (short) 4;
				});
				ArrayUtil.subtract(arr, (short) 2);
				ArrayUtil.fillArray(test, () -> {
					return (short) 2;
				});
				assertArrayEquals(test, arr);
			}

			@Test
			void intSub() {
				int[] arr = new int[10];
				int[] test = new int[10];

				ArrayUtil.fillArray(arr, () -> {
					return 4;
				});
				ArrayUtil.subtract(arr, 2);
				ArrayUtil.fillArray(test, () -> {
					return 2;
				});
				assertArrayEquals(test, arr);
			}

			@Test
			void longSub() {
				long[] arr = new long[10];
				long[] test = new long[10];

				ArrayUtil.fillArray(arr, () -> {
					return 4l;
				});
				ArrayUtil.subtract(arr, 2);
				ArrayUtil.fillArray(test, () -> {
					return 2l;
				});
				assertArrayEquals(test, arr);
			}

			@Test
			void floatSub() {
				float[] arr = new float[10];
				float[] test = new float[10];

				ArrayUtil.fillArray(arr, () -> {
					return 4f;
				});
				ArrayUtil.subtract(arr, 2f);
				ArrayUtil.fillArray(test, () -> {
					return 2f;
				});
				assertArrayEquals(test, arr);
			}

			@Test
			void doubleSub() {
				double[] arr = new double[10];
				double[] test = new double[10];

				ArrayUtil.fillArray(arr, () -> {
					return 4d;
				});
				ArrayUtil.subtract(arr, 2d);
				ArrayUtil.fillArray(test, () -> {
					return 2d;
				});
				assertArrayEquals(test, arr);
			}
		}

		@Nested
		class Multiply {
			@Test
			void byteMult() {
				byte[] arr = new byte[10];
				byte[] test = new byte[10];

				ArrayUtil.fillArray(arr, () -> {
					return (byte) 2;
				});
				ArrayUtil.multiply(arr, (byte) 2);
				ArrayUtil.fillArray(test, () -> {
					return (byte) 4;
				});
				assertArrayEquals(test, arr);
			}

			@Test
			void charMult() {
				char[] arr = new char[10];
				char[] test = new char[10];

				ArrayUtil.fillArray(arr, () -> {
					return (char) 2;
				});
				ArrayUtil.multiply(arr, (char) 2);
				ArrayUtil.fillArray(test, () -> {
					return (char) 4;
				});
				assertArrayEquals(test, arr);
			}

			@Test
			void shortMult() {
				short[] arr = new short[10];
				short[] test = new short[10];

				ArrayUtil.fillArray(arr, () -> {
					return (short) 2;
				});
				ArrayUtil.multiply(arr, (short) 2);
				ArrayUtil.fillArray(test, () -> {
					return (short) 4;
				});
				assertArrayEquals(test, arr);
			}

			@Test
			void intMult() {
				int[] arr = new int[10];
				int[] test = new int[10];

				ArrayUtil.fillArray(arr, () -> {
					return 2;
				});
				ArrayUtil.multiply(arr, 2);
				ArrayUtil.fillArray(test, () -> {
					return 4;
				});
				assertArrayEquals(test, arr);
			}

			@Test
			void longMult() {
				long[] arr = new long[10];
				long[] test = new long[10];

				ArrayUtil.fillArray(arr, () -> {
					return 2l;
				});
				ArrayUtil.multiply(arr, 2l);
				ArrayUtil.fillArray(test, () -> {
					return 4l;
				});
				assertArrayEquals(test, arr);
			}

			@Test
			void floatMult() {
				float[] arr = new float[10];
				float[] test = new float[10];

				ArrayUtil.fillArray(arr, () -> {
					return 2f;
				});
				ArrayUtil.multiply(arr, 2f);
				ArrayUtil.fillArray(test, () -> {
					return 4f;
				});
				assertArrayEquals(test, arr);
			}

			@Test
			void doubleMult() {
				double[] arr = new double[10];
				double[] test = new double[10];

				ArrayUtil.fillArray(arr, () -> {
					return 2d;
				});
				ArrayUtil.multiply(arr, 2d);
				ArrayUtil.fillArray(test, () -> {
					return 4d;
				});
				assertArrayEquals(test, arr);
			}
		}

		@Nested
		class Divide {
			@Test
			void byteDiv() {
				byte[] arr = new byte[10];
				byte[] test = new byte[10];

				ArrayUtil.fillArray(arr, () -> {
					return (byte) 4;
				});
				ArrayUtil.divide(arr, (byte) 2);
				ArrayUtil.fillArray(test, () -> {
					return (byte) 2;
				});
				assertArrayEquals(test, arr);
			}

			@Test
			void charDiv() {
				char[] arr = new char[10];
				char[] test = new char[10];

				ArrayUtil.fillArray(arr, () -> {
					return (char) 4;
				});
				ArrayUtil.divide(arr, (char) 2);
				ArrayUtil.fillArray(test, () -> {
					return (char) 2;
				});
				assertArrayEquals(test, arr);
			}

			@Test
			void shortDiv() {
				short[] arr = new short[10];
				short[] test = new short[10];

				ArrayUtil.fillArray(arr, () -> {
					return (short) 4;
				});
				ArrayUtil.divide(arr, (short) 2);
				ArrayUtil.fillArray(test, () -> {
					return (short) 2;
				});
				assertArrayEquals(test, arr);
			}

			@Test
			void intDiv() {
				int[] arr = new int[10];
				int[] test = new int[10];

				ArrayUtil.fillArray(arr, () -> {
					return 4;
				});
				ArrayUtil.divide(arr, 2);
				ArrayUtil.fillArray(test, () -> {
					return 2;
				});
				assertArrayEquals(test, arr);
			}

			@Test
			void longDiv() {
				long[] arr = new long[10];
				long[] test = new long[10];

				ArrayUtil.fillArray(arr, () -> {
					return 4l;
				});
				ArrayUtil.divide(arr, 2l);
				ArrayUtil.fillArray(test, () -> {
					return 2l;
				});
				assertArrayEquals(test, arr);
			}

			@Test
			void floatDiv() {
				float[] arr = new float[10];
				float[] test = new float[10];

				ArrayUtil.fillArray(arr, () -> {
					return 4f;
				});
				ArrayUtil.divide(arr, 2f);
				ArrayUtil.fillArray(test, () -> {
					return 2f;
				});
				assertArrayEquals(test, arr);
			}

			@Test
			void doubleDiv() {
				double[] arr = new double[10];
				double[] test = new double[10];

				ArrayUtil.fillArray(arr, () -> {
					return 4d;
				});
				ArrayUtil.divide(arr, 2d);
				ArrayUtil.fillArray(test, () -> {
					return 2d;
				});
				assertArrayEquals(test, arr);
			}
		}

	}

	@Nested
	class ComputeSortIndex {

		@Test
		void ascendingBool() {

			boolean[] b = { true, false, true };
			int sortedIndices[] = ArrayUtil.getSortedIndices(b, false);

			// Duplicate values
			assertAll(() -> {
				assertTrue(sortedIndices[0] > sortedIndices[1]);
			}, () -> {
				assertTrue(sortedIndices[2] > sortedIndices[1]);
			});
		}

		@Test
		void descendingBool() {

			boolean[] b = { true, false, true };
			int sortedIndices[] = ArrayUtil.getSortedIndices(b, true);

			// Duplicate values
			assertAll(() -> {
				assertTrue(sortedIndices[0] < sortedIndices[1]);
			}, () -> {
				assertTrue(sortedIndices[2] < sortedIndices[1]);
			});
		}

		@Test
		void descendingByte() {

			byte[] b = { 2, 1, 0, 4 };
			int[] sorted = { 3, 0, 1, 2 };
			int[] sortedIndices = ArrayUtil.getSortedIndices(b, true);

			assertArrayEquals(sorted, sortedIndices);

		}

		@Test
		void ascendingByte() {

			byte[] b = { 2, 1, 0, 4 };
			int[] sorted = { 2, 1, 0, 3 };
			int[] sortedIndices = ArrayUtil.getSortedIndices(b, false);
			assertArrayEquals(sorted, sortedIndices);
		}

		@Test
		void descendingChar() {

			char[] b = { 2, 1, 0, 4 };
			int[] sorted = { 3, 0, 1, 2 };
			int[] sortedIndices = ArrayUtil.getSortedIndices(b, true);

			assertArrayEquals(sorted, sortedIndices);

		}

		@Test
		void ascendingChar() {

			char[] b = { 2, 1, 0, 4 };
			int[] sorted = { 2, 1, 0, 3 };
			int[] sortedIndices = ArrayUtil.getSortedIndices(b, false);
			assertArrayEquals(sorted, sortedIndices);
		}

		@Test
		void descendingShort() {

			short[] b = { 2, 1, 0, 4 };
			int[] sorted = { 3, 0, 1, 2 };
			int[] sortedIndices = ArrayUtil.getSortedIndices(b, true);

			assertArrayEquals(sorted, sortedIndices);

		}

		@Test
		void ascendingShort() {

			short[] b = { 2, 1, 0, 4 };
			int[] sorted = { 2, 1, 0, 3 };
			int[] sortedIndices = ArrayUtil.getSortedIndices(b, false);
			assertArrayEquals(sorted, sortedIndices);
		}

		@Test
		void descendingInt() {

			int[] b = { 2, 1, 0, 4 };
			int[] sorted = { 3, 0, 1, 2 };
			int[] sortedIndices = ArrayUtil.getSortedIndices(b, true);

			assertArrayEquals(sorted, sortedIndices);

		}

		@Test
		void ascendingInt() {
			int[] b = { 2, 1, 0, 4 };
			int[] sorted = { 2, 1, 0, 3 };
			int[] sortedIndices = ArrayUtil.getSortedIndices(b, false);
			assertArrayEquals(sorted, sortedIndices);
		}

		@Test
		void descendingLong() {

			long[] b = { 2, 1, 0, 4 };
			int[] sorted = { 3, 0, 1, 2 };
			int[] sortedIndices = ArrayUtil.getSortedIndices(b, true);

			assertArrayEquals(sorted, sortedIndices);

		}

		@Test
		void ascendingLong() {
			long[] b = { 2, 1, 0, 4 };
			int[] sorted = { 2, 1, 0, 3 };
			int[] sortedIndices = ArrayUtil.getSortedIndices(b, false);
			assertArrayEquals(sorted, sortedIndices);
		}

		@Test
		void descendingFloat() {

			float[] b = { 2, 1, 0, 4 };
			int[] sorted = { 3, 0, 1, 2 };
			int[] sortedIndices = ArrayUtil.getSortedIndices(b, true);

			assertArrayEquals(sorted, sortedIndices);

		}

		@Test
		void ascendingFloat() {
			float[] b = { 2, 1, 0, 4 };
			int[] sorted = { 2, 1, 0, 3 };
			int[] sortedIndices = ArrayUtil.getSortedIndices(b, false);
			assertArrayEquals(sorted, sortedIndices);
		}

		@Test
		void descendingDouble() {

			double[] b = { 2, 1, 0, 4 };
			int[] sorted = { 3, 0, 1, 2 };
			int[] sortedIndices = ArrayUtil.getSortedIndices(b, true);

			assertArrayEquals(sorted, sortedIndices);

		}

		@Test
		void ascendingDouble() {
			double[] b = { 2, 1, 0, 4 };
			int[] sorted = { 2, 1, 0, 3 };
			int[] sortedIndices = ArrayUtil.getSortedIndices(b, false);
			assertArrayEquals(sorted, sortedIndices);
		}

		void negativeAscending() {

			int[] i = { -2, -5, 0, -3, 2 };
			int[] sorted = { 1, 3, 0, 2, 4 };
			int[] sortedIndices = ArrayUtil.getSortedIndices(i, false);
			assertArrayEquals(sorted, sortedIndices);
		}

		void negativeDecending() {

			int[] i = { -2, -5, 0, -3, 2 };
			int[] sorted = { 4, 2, 0, 3, 1 };
			int[] sortedIndices = ArrayUtil.getSortedIndices(i, true);
			assertArrayEquals(sorted, sortedIndices);
		}

	}

}

//		
//		@Test 
//		void deepArrayCopyEquivalent(){
//			Integer[] intArr = new Integer[10];
//			for(int i = 0; i < 10; i++) {
//				intArr[i] = Integer.valueOf(i);
//			}
//			Integer[] clone = ArrayUtil.deepArrayCopy(intArr);
//			for(int i = 0; i < 10; i++) {
//				assertEquals(intArr[i],clone[i]);
//			}
//		}
