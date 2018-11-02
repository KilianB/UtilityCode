package com.github.kilianB.graphics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.github.kilianB.ArrayUtil;

/**
 * @author Kilian
 *
 */
class ImageUtilTest {

	// No alpha image
	private static BufferedImage lena;

	// R(255)G(0)B(0) H(0)S(100)V(100) //Luminosity
	private static BufferedImage red;
	// R(0)G(255)B(0) H(120)S(100)V(100) //Luminosity
	private static BufferedImage green;
	// R(0)G(0)B(255) H(240)S(100)V(100) //Luminosity
	private static BufferedImage blue;
	// R(92)G(46)B(23) ~ H(20)S(75)V(36) //Luminosity
	private static BufferedImage brown;
	// R(92)G(46)B(23) ~ H(20)S(75)V(36) //Luminosity
	private static BufferedImage brownOpacity;

	//
	private static BufferedImage bw;

	@BeforeAll
	static void loadImage() {
		try {
			lena = ImageIO.read(ImageUtilTest.class.getClassLoader().getResourceAsStream("Lena.png"));
			bw = ImageIO.read(ImageUtilTest.class.getClassLoader().getResourceAsStream("BlackWhite.png"));
			red = ImageIO.read(ImageUtilTest.class.getClassLoader().getResourceAsStream("red.png"));
			green = ImageIO.read(ImageUtilTest.class.getClassLoader().getResourceAsStream("green.png"));
			blue = ImageIO.read(ImageUtilTest.class.getClassLoader().getResourceAsStream("blue.png"));
			brown = ImageIO.read(ImageUtilTest.class.getClassLoader().getResourceAsStream("brown.png"));
			brownOpacity = ImageIO.read(ImageUtilTest.class.getClassLoader().getResourceAsStream("brownOpacity.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Nested
	class FastPixel {

		@Test
		void hasAlphaFalse() {
			assertFalse(new ImageUtil.FastPixel(brown).hasAlpha());
		}

		@Test
		void hasAlphaTrue() {
			assertTrue(new ImageUtil.FastPixel(brownOpacity).hasAlpha());
		}

		@Test
		void getRGB() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(lena);
			for (int x = 0; x < lena.getWidth(); x++) {
				for (int y = 0; y < lena.getHeight(); y++) {
					assertEquals(lena.getRGB(x, y), fp.getRGB(x, y));
				}
			}
		}
		
		@Test
		void getRGBArray() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(lena);
			int[][] rgb = fp.getRGB();
			for (int x = 0; x < lena.getWidth(); x++) {
				for (int y = 0; y < lena.getHeight(); y++) {
					assertEquals(fp.getRGB(x, y),rgb[x][y]);
				}
			}
		}
		
		@Test
		void red() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(lena);
			for (int x = 0; x < lena.getWidth(); x++) {
				for (int y = 0; y < lena.getHeight(); y++) {
					int[] comp = ColorUtil.argbToComponents(lena.getRGB(x, y));
					assertEquals(comp[1], fp.getRed(x, y));
				}
			}
		}
		
		@Test
		void redArray() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(lena);
			int[][] red = fp.getRed();
			for (int x = 0; x < lena.getWidth(); x++) {
				for (int y = 0; y < lena.getHeight(); y++) {
					assertEquals(fp.getRed(x, y),red[x][y]);
				}
			}
		}
		

		@Test
		void green() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(lena);
			for (int x = 0; x < lena.getWidth(); x++) {
				for (int y = 0; y < lena.getHeight(); y++) {
					int[] comp = ColorUtil.argbToComponents(lena.getRGB(x, y));
					assertEquals(comp[2], fp.getGreen(x, y));
				}
			}
		}
		
		@Test
		void greenArray() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(lena);
			int[][] green = fp.getGreen();
			for (int x = 0; x < lena.getWidth(); x++) {
				for (int y = 0; y < lena.getHeight(); y++) {
					assertEquals(fp.getGreen(x, y),green[x][y]);
				}
			}
		}

		@Test
		void blue() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(lena);
			for (int x = 0; x < lena.getWidth(); x++) {
				for (int y = 0; y < lena.getHeight(); y++) {
					int[] comp = ColorUtil.argbToComponents(lena.getRGB(x, y));
					assertEquals(comp[3], fp.getBlue(x, y));
				}
			}
		}
		
		@Test
		void blueArray() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(lena);
			int[][] blue = fp.getBlue();
			for (int x = 0; x < lena.getWidth(); x++) {
				for (int y = 0; y < lena.getHeight(); y++) {
					assertEquals(fp.getBlue(x, y),blue[x][y]);
				}
			}
		}

		@Test
		void getRGBOpaque() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(brownOpacity);
			for (int x = 0; x < brownOpacity.getWidth(); x++) {
				for (int y = 0; y < brownOpacity.getHeight(); y++) {
					assertEquals(brownOpacity.getRGB(x, y), fp.getRGB(x, y));
				}
			}
		}

		@Test
		void alphaOpaque() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(brownOpacity);
			for (int x = 0; x < brownOpacity.getWidth(); x++) {
				for (int y = 0; y < brownOpacity.getHeight(); y++) {
					int[] comp = ColorUtil.argbToComponents(brownOpacity.getRGB(x, y));
					assertEquals(comp[0], fp.getAlpha(x, y));
				}
			}
		}

		@Test
		void redOpaque() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(brownOpacity);
			for (int x = 0; x < brownOpacity.getWidth(); x++) {
				for (int y = 0; y < brownOpacity.getHeight(); y++) {
					int[] comp = ColorUtil.argbToComponents(brownOpacity.getRGB(x, y));
					assertEquals(comp[1], fp.getRed(x, y));
				}
			}
		}

		@Test
		void greenOpaque() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(brownOpacity);
			for (int x = 0; x < brownOpacity.getWidth(); x++) {
				for (int y = 0; y < brownOpacity.getHeight(); y++) {
					int[] comp = ColorUtil.argbToComponents(brownOpacity.getRGB(x, y));
					assertEquals(comp[2], fp.getGreen(x, y));
				}
			}
		}

		@Test
		void blueOpaque() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(brownOpacity);
			for (int x = 0; x < brownOpacity.getWidth(); x++) {
				for (int y = 0; y < brownOpacity.getHeight(); y++) {
					int[] comp = ColorUtil.argbToComponents(brownOpacity.getRGB(x, y));
					assertEquals(comp[3], fp.getBlue(x, y));
				}
			}
		}

		@Test
		void rgbArray() {
			int arg[] = lena.getRGB(0, 0, lena.getWidth(), lena.getHeight(), null, 0, lena.getWidth());
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(lena);
			int[][] argFp = fp.getRGB();
			for (int x = 0; x < lena.getWidth(); x++) {
				for (int y = 0; y < lena.getHeight(); y++) {
					assertEquals(arg[ArrayUtil.twoDimtoOneDim(y, x, lena.getWidth())], argFp[x][y]);
				}
			}
		}

		@Test
		void lum() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(bw);

			for (int x = 0; x < bw.getWidth(); x++) {
				for (int y = 0; y < bw.getHeight(); y++) {

					if (y < 2) {
						// Black
						assertEquals(0, fp.getLuma(x, y));
					} else {
						// White
						assertEquals(255, fp.getLuma(x, y));
					}
				}
			}
		}
		
		
		@Test
		void lumArray() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(lena);
			int[][] lumArr = fp.getLuma();
			for (int x = 0; x < lena.getWidth(); x++) {
				for (int y = 0; y < lena.getHeight(); y++) {
					assertEquals(fp.getLuma(x,y),lumArr[x][y]);
				}
			}
		}
		
		@Test
		void lumInRange() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(lena);

			for (int x = 0; x < bw.getWidth(); x++) {
				for (int y = 0; y < bw.getHeight(); y++) {

					int lum = fp.getLuma(x, y);
					if (lum < 0 || lum > 255)
						fail("Luminosity ouside range");
				}
			}
		}

		@Test
		void hueBlackWhite() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(bw);
			// Invalid hue value. Defined as 0
			for (int x = 0; x < bw.getWidth(); x++) {
				for (int y = 0; y < bw.getHeight(); y++) {
					assertEquals(0, fp.getHue(x, y));
				}
			}
		}

		@Test
		void hueRed() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(red);

			for (int x = 0; x < red.getWidth(); x++) {
				for (int y = 0; y < red.getHeight(); y++) {
					assertEquals(0, fp.getHue(x, y));
				}
			}
		}

		@Test
		void hueGreen() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(green);
			for (int x = 0; x < green.getWidth(); x++) {
				for (int y = 0; y < green.getHeight(); y++) {
					assertEquals(120, fp.getHue(x, y));
				}
			}
		}

		@Test
		void hueBlue() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(blue);
			for (int x = 0; x < blue.getWidth(); x++) {
				for (int y = 0; y < blue.getHeight(); y++) {
					assertEquals(240, fp.getHue(x, y));
				}
			}
		}

		@Test
		void hueBrown() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(brown);
			for (int x = 0; x < brown.getWidth(); x++) {
				for (int y = 0; y < brown.getHeight(); y++) {
					assertEquals(20, fp.getHue(x, y));
				}
			}
		}
		
		@Test
		void satBlackWhite() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(bw);
			// Invalid hue value. Defined as 0
			for (int x = 0; x < bw.getWidth(); x++) {
				for (int y = 0; y < bw.getHeight(); y++) {
					assertEquals(0, fp.getSat(x, y));
				}
			}
		}

		@Test
		void satRed() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(red);

			for (int x = 0; x < red.getWidth(); x++) {
				for (int y = 0; y < red.getHeight(); y++) {
					assertEquals(1, fp.getSat(x, y));
				}
			}
		}

		@Test
		void satGreen() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(green);
			for (int x = 0; x < green.getWidth(); x++) {
				for (int y = 0; y < green.getHeight(); y++) {
					assertEquals(1, fp.getSat(x, y));
				}
			}
		}

		@Test
		void satBlue() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(blue);
			for (int x = 0; x < blue.getWidth(); x++) {
				for (int y = 0; y < blue.getHeight(); y++) {
					assertEquals(1, fp.getSat(x, y));
				}
			}
		}

		@Test
		void satBrown() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(brown);
			for (int x = 0; x < brown.getWidth(); x++) {
				for (int y = 0; y < brown.getHeight(); y++) {
					assertEquals(0.75, fp.getSat(x, y));
				}
			}
		}
		
		@Test
		void valBlackWhite() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(bw);
			for (int x = 0; x < bw.getWidth(); x++) {
				for (int y = 0; y < bw.getHeight(); y++) {
					if(y < 2) {
						//Black
						assertEquals(0, fp.getVal(x, y));
					}else {
						//White
						assertEquals(255, fp.getVal(x, y));
					}
					
				}
			}
		}

		@Test
		void valRed() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(red);

			for (int x = 0; x < red.getWidth(); x++) {
				for (int y = 0; y < red.getHeight(); y++) {
					assertEquals(255, fp.getVal(x, y));
				}
			}
		}

		@Test
		void valGreen() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(green);
			for (int x = 0; x < green.getWidth(); x++) {
				for (int y = 0; y < green.getHeight(); y++) {
					assertEquals(255, fp.getVal(x, y));
				}
			}
		}

		@Test
		void valBlue() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(blue);
			for (int x = 0; x < blue.getWidth(); x++) {
				for (int y = 0; y < blue.getHeight(); y++) {
					assertEquals(255, fp.getVal(x, y));
				}
			}
		}

		@Test
		void valBrown() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(brown);
			for (int x = 0; x < brown.getWidth(); x++) {
				for (int y = 0; y < brown.getHeight(); y++) {
					assertEquals(92, fp.getVal(x, y));
				}
			}
		}

	}

	void getScaledInstanceSize() {
		BufferedImage scaled = ImageUtil.getScaledInstance(lena,10,10);
		assertEquals(10,scaled.getWidth());
		assertEquals(10,scaled.getHeight());
	}
	
}
