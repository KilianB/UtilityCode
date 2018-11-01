package com.github.kilianB.graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

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
	 * High performant access of RGB/YCrCb Data.
	 * 
	 * Should run faster by a magnitude (factor 10) compared to the original java
	 * getRGB methods.
	 * 
	 * @author Kilian
	 * @since 1.3.0
	 */
	public static class FastPixel {

		private static final int ALPHA_MASK = 255 << 24;

		private boolean alpha;
		private int alphaOffset = 0;
		private int bytesPerColor;

		private int width;
		private int height;

		// Raw data
		private byte[] imageData;

		public FastPixel(BufferedImage bImage) {

			// Do we have to deal with the windows byte buffer bug?
			imageData = ((DataBufferByte) bImage.getRaster().getDataBuffer()).getData();

			// Alpha channel has it's own band? Really?
			if (bImage.getAlphaRaster() != null) {
				alphaOffset = 1;
				alpha = true;
				bytesPerColor = 4;
			} else {
				alphaOffset = 0;
				alpha = false;
				bytesPerColor = 3;
			}
			width = bImage.getWidth();
			height = bImage.getHeight();
		}

		/**
		 * Returns an integer pixel in the default RGB color model(TYPE_INT_ARGB). There
		 * are only 8-bits of precision for each color component in the returned data
		 * when using this method. An ArrayOutOfBoundsException may be thrown if the
		 * coordinates are not in bounds.
		 * 
		 * @param x the X coordinate of the pixel from which to get the pixel in the
		 *          default RGB color model
		 * @param y the Y coordinate of the pixel from which to get the pixel in the
		 *          default RGB color model
		 * 
		 * @since 1.3.0
		 */
		public int getRGB(int x, int y) {
			int index = getOffset(x, y);
			// Red
			return alpha ? (imageData[index++] & 0xFF) << 24
					: ALPHA_MASK | ((imageData[index++] & 0xFF)) | ((imageData[index++] & 0xFF) << 8)
							| ((imageData[index++] & 0xFF) << 16);

		}

		public int[][] getRGB() {
			int[][] rgb = new int[width][height];
			int x = 0;
			int y = 0;
			for (int i = 0; i < imageData.length; i++) {
				// We could use the getRGB(x,y) method. but lets inline some calls
				int argb;
				argb = alpha ? (imageData[i++] & 0xFF) << 24 : ALPHA_MASK;
				// Red
				argb |= (imageData[i++] & 0xFF) | (imageData[i++] & 0xFF) << 8 | (imageData[i] & 0xFF) << 16;

				rgb[x][y] = argb;
				x++;
				if (x >= width) {
					x = 0;
					y++;
				}
			}
			return rgb;
		}

		public int getAlpha(int x, int y) {
			if (!alpha)
				return -1;
			return imageData[getOffset(x, y)] & 0xFF;
		}

		public int getRed(int x, int y) {
			return imageData[getOffset(x, y) + alphaOffset + 2] & 0xFF;
		}

		public int[][] getRed() {
			int[][] red = new int[width][height];
			int x = 0;
			int y = 0;
			for (int i = 0; i < imageData.length; i++) {
				// We could use the getRGB(x,y) method. but lets inline some calls
				if (alpha)
					i++;
				red[x][y] = (imageData[i] & 0xFF) << 16;
				i += 3;
				x++;
				if (x >= width) {
					x = 0;
					y++;
				}
			}
			return red;
		}

		public int getGreen(int x, int y) {
			return imageData[getOffset(x, y) + alphaOffset + 1] & 0xFF;
		}

		public int getBlue(int x, int y) {
			return imageData[getOffset(x, y) + alphaOffset] & 0xFF;
		}

		// Hue saturation brightness

		// YCrCb

		/**
		 * 
		 * The Y(Luma) component of the YCrCb color model
		 * 
		 * 0 - 255
		 * 
		 * @return the Y
		 */
		public int getLuma(int x, int y) {
			int index = (y * bytesPerColor * width) + (x * bytesPerColor);
			if (alpha)
				index++;
			int luma = (int) Math.round((imageData[index++] & 0xFF) * ColorUtil.LUMA_RED
					+ (imageData[index++] & 0xFF) * ColorUtil.LUMA_GREEN
					+ (imageData[index++] & 0xFF) * ColorUtil.LUMA_BLUE);
			return luma > 255 ? 255 : luma;
		}

		//TODO fix and check
		public int getHue(int x, int y) {

			int offset = getOffset(x, y);
			int blue = imageData[offset + alphaOffset] & 0xFF;
			int green = imageData[offset + alphaOffset + 1] & 0xFF;
			int red = imageData[offset + alphaOffset + 2] & 0xFF;

			int min = Math.min(blue, Math.min(green, red));
			int max = Math.max(blue, Math.max(green, red));

			if (max == min)
				return 0;

			double range = max - min;
			
			double h;
			if (red == max) {
				h = 60 * ((green - blue) / range);
			} else if (green == max) {
				h = 60 * (2 + (blue - red) / range);
			} else {
				h = 60 * (4 + (red - green) / range);
			}

			int hue = (int) Math.round(h);

			if (hue < 0)
				hue += 360;

			return hue;
		}

//		public int getSat() {
//			
//		}
//		
//		public int getVal() {
//			
//		}

		private int getOffset(int x, int y) {
			return (y * bytesPerColor * width) + (x * bytesPerColor);
		}

	}

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
	 * color using this approach is a rather expensive operation.
	 * <p>
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
	 * Calculate the average color of this image. The average color is determined by
	 * summing the squared argb component of each pixel and determining the mean
	 * value of these.
	 * 
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
