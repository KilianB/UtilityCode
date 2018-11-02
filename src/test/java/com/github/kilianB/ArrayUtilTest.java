package com.github.kilianB;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.geom.Point2D;
import java.util.Arrays;

import org.junit.jupiter.api.Disabled;
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
		assertEquals(null,intArr);
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
				for(int m = 0; m < intArr[0][0].length; m++) {
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

	//Search
	@Test 
	void linearSearch() {
		Integer[] arr = new Integer[10];
		MutableInteger i = new MutableInteger(0);
		ArrayUtil.fillArray(arr,()->{return i.getAndIncrement();});
		assertEquals(3,ArrayUtil.linearSearch(arr,3));
	}
	
	@Test 
	void linearSearchNotInArray() {
		Integer[] arr = new Integer[10];
		MutableInteger i = new MutableInteger(0);
		ArrayUtil.fillArray(arr,()->{return i.getAndIncrement();});
		assertEquals(-1,ArrayUtil.linearSearch(arr,10));
	}
	
	@Test 
	void linearSearchRange() {
		Integer[] arr = new Integer[10];
		MutableInteger i = new MutableInteger(0);
		ArrayUtil.fillArray(arr,()->{return i.getAndIncrement();});
		assertEquals(3,ArrayUtil.linearSearch(arr,3,1,4));
	}
	
	@Test 
	void linearSearchRangeNotInArray() {
		Integer[] arr = new Integer[10];
		MutableInteger i = new MutableInteger(0);
		ArrayUtil.fillArray(arr,()->{return i.getAndIncrement();});
		assertEquals(-1,ArrayUtil.linearSearch(arr,10,1,4));
	}
	
	@Test 
	void linearSearchResultInLower() {
		Integer[] arr = new Integer[10];
		MutableInteger i = new MutableInteger(0);
		ArrayUtil.fillArray(arr,()->{return i.getAndIncrement();});
		assertEquals(-1,ArrayUtil.linearSearch(arr,3,4,6));
	}
	
	@Test 
	void linearSearchRangeInUpper() {
		Integer[] arr = new Integer[10];
		MutableInteger i = new MutableInteger(0);
		ArrayUtil.fillArray(arr,()->{return i.getAndIncrement();});
		assertEquals(-1,ArrayUtil.linearSearch(arr,5,1,4));
	}
	
	@Test 
	void frontBackSearch() {
		Integer[] arr = new Integer[10];
		MutableInteger i = new MutableInteger(0);
		ArrayUtil.fillArray(arr,()->{return i.getAndIncrement();});
		assertEquals(3,ArrayUtil.frontBackSearch(arr,3));
	}
	
	@Test 
	void frontBackSearchNotInArray() {
		Integer[] arr = new Integer[10];
		MutableInteger i = new MutableInteger(0);
		ArrayUtil.fillArray(arr,()->{return i.getAndIncrement();});
		assertEquals(-1,ArrayUtil.frontBackSearch(arr,10));
	}
	
	@Test 
	void frontBackSearchRange() {
		Integer[] arr = new Integer[10];
		MutableInteger i = new MutableInteger(0);
		ArrayUtil.fillArray(arr,()->{return i.getAndIncrement();});
		assertEquals(3,ArrayUtil.frontBackSearch(arr,3,1,4));
	}
	
	@Test 
	void frontBackSearchRangeNotInArray() {
		Integer[] arr = new Integer[10];
		MutableInteger i = new MutableInteger(0);
		ArrayUtil.fillArray(arr,()->{return i.getAndIncrement();});
		assertEquals(-1,ArrayUtil.frontBackSearch(arr,10,1,4));
	}
	
	@Test 
	void frontBackSearchResultInLower() {
		Integer[] arr = new Integer[10];
		MutableInteger i = new MutableInteger(0);
		ArrayUtil.fillArray(arr,()->{return i.getAndIncrement();});
		assertEquals(-1,ArrayUtil.frontBackSearch(arr,3,4,6));
	}
	
	@Test 
	void frontBackSearchRangeInUpper() {
		Integer[] arr = new Integer[10];
		MutableInteger i = new MutableInteger(0);
		ArrayUtil.fillArray(arr,()->{return i.getAndIncrement();});
		assertEquals(-1,ArrayUtil.frontBackSearch(arr,5,1,4));
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

}
