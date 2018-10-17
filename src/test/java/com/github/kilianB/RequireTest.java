/**
 * @author Kilian
 *
 */
package com.github.kilianB;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RequireTest {

	@Nested
	class RequirePositiveValue {

		@Nested
		class Integer {

			@Test
			void testZero() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.positiveValue(0);
				});
			}

			@Test
			void testMinimal() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.positiveValue(java.lang.Integer.MIN_VALUE);
				});
			}

			@Test
			void testMaximal() {
				assertEquals(java.lang.Integer.MAX_VALUE,
						(int) Require.positiveValue(java.lang.Integer.MAX_VALUE));
			}

			@Test
			void testValid() {
				assertEquals(1, (int) Require.positiveValue(1));
			}

			@Test
			void testInvalid() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.positiveValue(-1);
				});
			}
		}

		@Nested
		class Long {

			@Test
			void testZero() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.positiveValue(0L);
				});
			}

			@Test
			void testMinimal() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.positiveValue(java.lang.Long.MIN_VALUE);
				});
			}

			@Test
			void testMaximal() {
				assertEquals(java.lang.Long.MAX_VALUE, (long) Require.positiveValue(java.lang.Long.MAX_VALUE));
			}

			@Test
			void testValid() {
				assertEquals(1L, (long)Require.positiveValue(1L));
			}

			@Test
			void testInvalid() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.positiveValue(-1L);
				});
			}
		}

		@Nested
		class Double {

			@Test
			void testZero() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.positiveValue(0d);
				});
			}

			@Test
			void testMinimal() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.positiveValue(-java.lang.Double.MAX_VALUE);
				});
			}

			@Test
			void testMaximal() {
				assertEquals(java.lang.Double.MAX_VALUE,
						(double) Require.positiveValue(java.lang.Double.MAX_VALUE));	
			}

			@Test
			void testValid() {
				assertEquals(1d, (double) Require.positiveValue(1d));
			}

			@Test
			void testInvalid() {
				assertThrows(IllegalArgumentException.class, () -> {
					Require.positiveValue(-1d);
				});
			}
		}

	}

	@Nested
	class RequireInRange{
		
		@Test
		void lowerBound(){
			int value = Require.inRange(0,0,1,"");
			assertEquals(value,0);
		}
		
		@Test
		void upperBound(){
			int value = Require.inRange(1,0,1,"");
			assertEquals(value,1);
		}
		
		void valid() {
			int value = Require.inRange(1,0,2,"");
			assertEquals(value,2);
		}
		
		@Test
		void lowerBoundFail(){
			assertThrows(IllegalArgumentException.class, () -> {
				Require.inRange(-1,0,1,"");
			});
		}
		
		@Test
		void upperBoundFail(){
			assertThrows(IllegalArgumentException.class, () -> {
				Require.inRange(2,0,1,"");
			});
		}
	}
	
	@Nested
	class NonNull{
		
		@Test
		void empty() {
			Integer[] intArr = new Integer[] {};
			Require.nonNull(intArr,"");
		}
		
		@Test
		void valid() {
			Integer[] intArr = new Integer[] {new Integer(1),Integer.MAX_VALUE,Integer.MIN_VALUE};
			Require.nonNull(intArr,"");
		}
		
		@Test
		void invalid() {
			Integer[] intArr = new Integer[] {null,Integer.MAX_VALUE,Integer.MIN_VALUE};
			assertThrows(IllegalArgumentException.class,()->{Require.nonNull(intArr,"");});
		}
		
		@Test
		void emptyList() {
			Require.nonNull(new ArrayList(),"");
		}
		
		@Test
		void validList() {
			Integer[] intArr = new Integer[] {new Integer(1),Integer.MAX_VALUE,Integer.MIN_VALUE};
			Require.nonNull(Arrays.asList(intArr),"");
		}
		
		@Test
		void invalidList() {
			Integer[] intArr = new Integer[] {null,Integer.MAX_VALUE,Integer.MIN_VALUE};
			assertThrows(IllegalArgumentException.class,()->{Require.nonNull(Arrays.asList(intArr),"");});
		}
		
	}
	
}