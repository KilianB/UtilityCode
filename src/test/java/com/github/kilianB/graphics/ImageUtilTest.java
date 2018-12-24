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

	//
	static BufferedImage bw;

	@BeforeAll
	static void loadImage() {
		try {
			lena = ImageIO.read(ImageUtilTest.class.getClassLoader().getResourceAsStream("Lena.png"));
			bw = ImageIO.read(ImageUtilTest.class.getClassLoader().getResourceAsStream("BlackWhite.png"));
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
