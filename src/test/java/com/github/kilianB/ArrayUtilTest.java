package com.github.kilianB;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Objects;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author Kilian
 *
 */
class ArrayUtilTest {

		@Test
		void fillArray() {
			Integer fill = Integer.MIN_VALUE;
			Integer[] intArr = new Integer[10];
			ArrayUtil.fillArray(intArr,() -> {return fill;});
			for(Integer i : intArr) {
				assertEquals(fill,i);
			}
		}
		
		@Test 
		void deepArrayCopyShallowOuter(){
			Integer[] intArr = new Integer[10];
			for(int i = 0; i < 10; i++) {
				intArr[i] = Integer.valueOf(i);
			}
			Integer[] clone = ArrayUtil.deepArrayCopy(intArr);
			assertTrue(clone!=intArr);
		}
		
		@Test 
		void deepArrayCopyElements(){
			Integer[] intArr = new Integer[10];
			for(int i = 0; i < 10; i++) {
				intArr[i] = Integer.valueOf(i);
			}
			Integer[] clone = ArrayUtil.deepArrayCopy(intArr);
			assertTrue(Arrays.deepEquals(intArr,clone));
		}
		
		//Now get to the nested part
		
		@Test 
		void deepArrayCopyElementsMultiDim(){
			Integer[][] intArr = new Integer[10][];
			for(int i = 0; i < 10; i++) {
				intArr[i] = new Integer[4];
				for(int j = 0; j < 4; j++) {
					intArr[i][j] = 1;
				}
			}
			Integer[][] clone = ArrayUtil.deepArrayCopy(intArr);
			assertTrue(Arrays.deepEquals(intArr,clone));
		}
		
		@Test 
		void deepArrayCopyElementsMultiDimNullValues(){
			Integer[][] intArr = new Integer[10][];
			for(int i = 0; i < 10; i++) {
				intArr[i] = new Integer[4];
			}
			Integer[][] clone = ArrayUtil.deepArrayCopy(intArr);
			assertTrue(Arrays.deepEquals(intArr,clone));
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
				if(i%2 == 0) {
					array[i] = new javafx.geometry.Point2D(0,0);
				}else {
					Object[] o = new Object[4];
					array[i] = o;
					for(int j = 0; j < 4; j++) {
						o[j] = new javafx.geometry.Point2D(0,0);
					}
				}
			}
			Object[] clone = ArrayUtil.deepArrayCopy(array);
			Arrays.deepEquals(array,clone);
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
