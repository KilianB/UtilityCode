package com.github.kilianB;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * @author Kilian
 *
 */
class MathUtilTest {

	@Nested
	class ClosestDivisibleInteger{
		@Test
		void findClosestDivisibleIntegerTest() {
			// Error
			assertThrows(java.lang.ArithmeticException.class, () -> {
				MathUtil.findClosestDivisibleInteger(0, 0);
			});
		}

		@Test
		void findClosestDivisibleIntegerIdentityTest() {
			assertEquals(1, MathUtil.findClosestDivisibleInteger(1, 1));
		}

		@Test
		void findClosestDivisibleIntegerPossibleDivisionTest() {
			assertEquals(4, MathUtil.findClosestDivisibleInteger(4, 2));
		}

		@Test
		void findClosestDivisibleIntegerUpperBoundTest() {
			assertEquals(9, MathUtil.findClosestDivisibleInteger(8, 3));
		}

		@Test
		void findClosestDivisibleIntegerLowerBoundTest() {
			assertEquals(8, MathUtil.findClosestDivisibleInteger(9, 4));
		}

		@Test
		void findClosestDivisibleIntegerEquidistantTest() {
			// 3 to each side 12 - 15 - 18
			assertEquals(18, MathUtil.findClosestDivisibleInteger(15, 6));
		}

		@Test
		void findClosestDivisibleIntegerIdentityNegativeDivisorTest() {
			assertEquals(1, MathUtil.findClosestDivisibleInteger(1, -1));
		}
		
		@Test
		void findClosestDivisibleIntegerIdentityNegativeDivisorTest2() {
			assertEquals(2, MathUtil.findClosestDivisibleInteger(2, -2));
		}

		@Test
		void findClosestDivisibleIntegerPossibleDivisionNegativeDivisorTest() {
			assertEquals(4, MathUtil.findClosestDivisibleInteger(4, -2));
		}

		@Test
		void findClosestDivisibleIntegerUpperBoundNegativeDivisorTest() {
			assertEquals(12, MathUtil.findClosestDivisibleInteger(11, -3));
		}

		@Test
		void findClosestDivisibleIntegerLowerBoundNegativeDivisorTest() {
			assertEquals(6, MathUtil.findClosestDivisibleInteger(7, -3));
		}

		@Test
		void findClosestDivisibleIntegerIdentityNegativeDividendTest() {
			assertEquals(-1, MathUtil.findClosestDivisibleInteger(-1, 1));
		}
		
		@Test
		void findClosestDivisibleIntegerIdentityNegativeDividendTest2() {
			assertEquals(-2, MathUtil.findClosestDivisibleInteger(-2, 2));
		}

		@Test
		void findClosestDivisibleIntegerPossibleDivisionNegativeDividendTest() {
			assertEquals(-4, MathUtil.findClosestDivisibleInteger(-4, 2));
		}

		@Test
		void findClosestDivisibleIntegerLowerBoundNegativeDividendTest() {
			assertEquals(-12, MathUtil.findClosestDivisibleInteger(-11, 3));
		}

		@Test
		void findClosestDivisibleIntegerUpperBoundNegativeDividendTest() {
			assertEquals(-6, MathUtil.findClosestDivisibleInteger(-7, 3));
		}
		
		@Test
		void findClosestDivisibleIntegerEquidistantNegativeDividendTest() {
			// 3 to each side 12 - 15 - 18
			assertEquals(18, MathUtil.findClosestDivisibleInteger(15, 6));
		}
	}
	
	@Nested
	class ClampNumber{
		@Test
		void clampNumberIdentity() {
			assertEquals(0,(int)MathUtil.clampNumber(0,0,0));
		}
		
		@Test
		void clampNumberExactUpper() {
			assertEquals(1,(int)MathUtil.clampNumber(1,0,1));
		}
		
		@Test
		void clampNumberExactLower() {
			assertEquals(0,(int)MathUtil.clampNumber(0,0,1));
		}
		
		@Test
		void clampNumberUpper() {
			assertEquals(1,(int)MathUtil.clampNumber(2,0,1));
		}
		
		@Test
		void clampNumberLower() {
			assertEquals(0,(int)MathUtil.clampNumber(-1,0,1));
		}
	}
	
	@Nested 
	class FractionalPart{
		@Test
		void fractionalNothing() {
			assertEquals(0,MathUtil.getFractionalPart(4d));
		}
		
		@Test
		void fractionalDefined() {
			assertEquals(0.123,MathUtil.getFractionalPart(4.123d),1e-6);
		}
		
		@Test
		void fractionalDefinedNegative() {
			assertEquals(-0.123,MathUtil.getFractionalPart(-4.123d),1e-6);
		}
	}
	
	
	@Nested
	class DoubleEquals{
		@Test
		void isDoubleEqualsValid() {
			assertTrue(MathUtil.isDoubleEquals(1d,1d,0));
		}
		
		@Test
		void isDoubleEqualsInvalid() {
			assertFalse(MathUtil.isDoubleEquals(2d,1d,0));
		}
		
		@Test
		void isDoubleEqualsNegativeValid() {
			assertTrue(MathUtil.isDoubleEquals(-1d,-1d,0));
		}
		
		@Test
		void isDoubleEqualsNegativeInValid() {
			assertFalse(MathUtil.isDoubleEquals(-2d,1d,0));
		}
		
		@Test
		void isDoubleEqualsValidEpisolon() {
			assertTrue(MathUtil.isDoubleEquals(1.2d,1d,1));
		}
		
		@Test
		void isDoubleEqualsNegativeValidEpisolon() {
			assertTrue(MathUtil.isDoubleEquals(-1.2d,-1d,1));
		}
		
		@Test
		void isDoubleEqualsInvalidEpsilon() {
			assertFalse(MathUtil.isDoubleEquals(2.2d,2d,0.1d));
		}
	}
	
	@Nested
	class IsNumeric{
		
		@Test
		void intPrimitive() {
			assertTrue(MathUtil.isNumeric(1));
		}
		
		@Test
		void bytePrimitive() {
			assertTrue(MathUtil.isNumeric((byte)1));
		}
		
		@Test
		void booleanPrimitive() {
			assertFalse(MathUtil.isNumeric(false));
		}
		
		@Test
		void longPrimitive() {
			assertTrue(MathUtil.isNumeric(1l));
		}
		
		@Test
		void floatPrimitive() {
			assertTrue(MathUtil.isNumeric(1f));
		}
		
		@Test
		void doublePrimitive() {
			assertTrue(MathUtil.isNumeric(1d));
		}
		
		@Test
		void charPrimitive() {
			assertFalse(MathUtil.isNumeric('c'));
		}
		
		@Test
		void string() {
			assertFalse(MathUtil.isNumeric("HelloWorld"));
		}
		
		@Test
		void object() {
			assertFalse(MathUtil.isNumeric(new Object()));
		}
		
		//Numeric Objects
		@Test
		void Byte() {
			assertTrue(MathUtil.isNumeric(Byte.valueOf((byte)1)));
		}
		
		@Test
		void Boolean() {
			assertFalse(MathUtil.isNumeric(Boolean.TRUE));
		}
		
		@Test
		void Long() {
			assertTrue(MathUtil.isNumeric(Long.valueOf(0)));
		}
		
		@Test
		void Float() {
			assertTrue(MathUtil.isNumeric(Float.valueOf(0.2f)));
		}
		
		@Test
		void Double() {
			assertTrue(MathUtil.isNumeric(Double.valueOf(0.1d)));
		}
		
		@Test
		void Character() {
			assertFalse(MathUtil.isNumeric(Character.valueOf('c')));
		}
		
	}
}
