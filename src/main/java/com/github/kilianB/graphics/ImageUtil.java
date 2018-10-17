package com.github.kilianB.graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

/**
 * @author Kilian
 *
 */
public class ImageUtil {

	/**
	 * Resize the buffered image
	 * 
	 * @param source the source image
	 * @param width  the new width
	 * @param height the new height
	 * @return the resized image
	 * @since 1.0.0
	 */
	public static BufferedImage getScaledInstance(BufferedImage source, int width, int height) {
		BufferedImage target = new BufferedImage(width, height, source.getType());
		java.awt.Image scaled = source.getScaledInstance(width, height, 0x1);

		Graphics g = target.getGraphics();
		g.drawImage(scaled, 0, 0, null);
		g.dispose();
		return target;
	}

	/**
	 * Calculate the interpolated average color of the image
	 * 
	 * @param image the source image
	 * @return the average color of the image
	 * @since 1.0.0
	 */
	public static Color interpolateColor(Image image) {
		BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
		bImage = ImageUtil.getScaledInstance(bImage, 1, 1);
		int argb = bImage.getRGB(0, 0);
		return ColorUtil.argbToFXColor(argb);
	}

	/**
	 * Return the dominant color of this image. The dominant color is the color that
	 * most often occurred in this image. Be aware that calculating the average
	 * color using this approach is a rather expensive operation. <p>
	 * 
	 * @param image The source image
	 * @return the dominant color of this image
	 * @since 1.0.0
	 */
	public static Color dominantColor(Image image) {

		HashMap<Integer, Integer> colorCount = new HashMap<>();

		PixelReader pr = image.getPixelReader();

		int[] pixels = new int[(int) (image.getWidth() * image.getHeight())];

		pr.getPixels(0, 0, (int) image.getWidth(), (int) image.getHeight(), PixelFormat.getIntArgbInstance(), pixels, 0,
				(int) image.getWidth());

		for (int argb : pixels) {
			// Do we even need to put it back in? Can't be simply change the Integer object?
			Integer curValue = colorCount.get(argb);
			colorCount.put(argb, curValue == null ? 1 : curValue.intValue() + 1);
		}

		int argb = colorCount.entrySet().stream().max((entry, entry2) -> entry.getValue().compareTo(entry2.getValue()))
				.get().getKey().intValue();

		return ColorUtil.argbToFXColor(argb);
	}

	/**
	 * Calculate the average color of this image. The average color is determined by summing the squared argb component
	 * of each pixel and determining the mean value of these.
	 * @param image The source image
	 * @return The average mean color of this image
	 * @since 1.0.0
	 */
	public static Color meanColor(Image image) {

		PixelReader pr = image.getPixelReader();
		int[] pixels = new int[(int) (image.getWidth() * image.getHeight())];

		pr.getPixels(0, 0, (int) image.getWidth(), (int) image.getHeight(), PixelFormat.getIntArgbInstance(), pixels, 0,
				(int) image.getWidth());

		double meanAlpha = 0;
		double meanRed = 0;
		double meanBlue = 0;
		double meanGreen = 0;

		final int pixelCount = pixels.length;

		for (int argb : pixels) {

			// ARGB values are not linearly scaled. therefore square roots are necessary.
			int[] colorComponents = ColorUtil.argbToComponents(argb);

			meanAlpha += (Math.pow(colorComponents[0], 2) / pixelCount);
			meanRed += (Math.pow(colorComponents[1], 2) / pixelCount);
			meanGreen += (Math.pow(colorComponents[2], 2) / pixelCount);
			meanBlue += (Math.pow(colorComponents[3], 2) / pixelCount);
		}

		int argbMean = ColorUtil.componentsToARGB((int) Math.sqrt(meanAlpha) * 255, (int) Math.sqrt(meanRed),
				(int) Math.sqrt(meanGreen), (int) Math.sqrt(meanBlue));

		return ColorUtil.argbToFXColor(argbMean);
	}

}
