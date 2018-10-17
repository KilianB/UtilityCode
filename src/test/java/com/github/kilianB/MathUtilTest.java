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
}
