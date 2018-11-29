package com.github.kilianB.graphics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.BeforeAll;

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
	static BufferedImage bw;

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

	void getScaledInstanceSize() {
		BufferedImage scaled = ImageUtil.getScaledInstance(lena, 10, 10);
		assertEquals(10, scaled.getWidth());
		assertEquals(10, scaled.getHeight());
	}

}
