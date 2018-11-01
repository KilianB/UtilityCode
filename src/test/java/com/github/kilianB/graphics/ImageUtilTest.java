package com.github.kilianB.graphics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.BeforeAll;
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
	private static BufferedImage bw;

	@BeforeAll
	static void loadImage() {
		try {
			lena = ImageIO.read(ImageUtilTest.class.getClassLoader().getResourceAsStream("Lena.png"));
			bw = ImageIO.read(ImageUtilTest.class.getClassLoader().getResourceAsStream("BlackWhite.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Nested
	class FastPixel {

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
		void hue() {
			ImageUtil.FastPixel fp = new ImageUtil.FastPixel(lena);

			for (int x = 0; x < bw.getWidth(); x++) {
				for (int y = 0; y < bw.getHeight(); y++) {
					System.out.println(fp.getHue(x,y));
				}
			}
		}

	}

}
