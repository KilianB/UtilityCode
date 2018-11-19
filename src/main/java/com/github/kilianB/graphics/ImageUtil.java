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
	 * High performant access of RGB/YCrCb/HSV Data.
	 * 
	 * <p>
	 * RGB methods run faster by a magnitude (factor 10) compared to the original
	 * java getRGB method.
	 * 
	 * <p>
	 * Currently only ARGB and RGB image types are supported.
	 * 
	 * @author Kilian
	 * @since 1.3.0
	 */
	public static class FastPixel {

		/** Full alpha constant */
		private static final int ALPHA_MASK = 255 << 24;

		/** True if the underlying image has an alpha component */
		private boolean alpha;
		/** Offset used in case alpha is present */
		private int alphaOffset = 0;
		/** Bytes used to represent a single pixel */
		private int bytesPerColor;

		/** Width of the image */
		private int width;

		/** Height of the image */
		private int height;

		/** Raw data */
		private byte[] imageData;

		/**
		 * Constructs a fast pixel object with the underlying buffered image.
		 * 
		 * <p>
		 * Note that calling this method may cause this DataBufferobject to be
		 * incompatible with performance optimizations used by some implementations
		 * (such as caching an associated image in video memory).
		 * 
		 * @param bImage The buffered image to extract data from
		 * @since 1.3.0
		 */
		public FastPixel(BufferedImage bImage) {

			// Do we have to deal with the windows byte buffer bug?
			imageData = ((DataBufferByte) bImage.getRaster().getDataBuffer()).getData();

			// Alpha channel has it's own band? Really?
			if (bImage.getColorModel().hasAlpha()) {
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
		 * @return an integer pixel in the default RGB color model and default sRGB
		 *         colorspace.
		 * @since 1.3.0
		 */
		public int getRGB(int x, int y) {
			int index = getOffset(x, y);
			// Red
			return (alpha ? (imageData[index++] & 0xFF) << 24 : ALPHA_MASK) | ((imageData[index++] & 0xFF))
					| ((imageData[index++] & 0xFF) << 8) | ((imageData[index++] & 0xFF) << 16);

		}

		/**
		 * Returns the rgb values of the entire image in an 2 d array in the default RGB
		 * color model(TYPE_INT_ARGB). There are only 8-bits of precision for each color
		 * component in the returned data when using this method. An
		 * ArrayOutOfBoundsException may be thrown if the coordinates are not in bounds.
		 * 
		 * @return a 2d integer array containing the argb values of the image
		 * @since 1.3.0
		 */
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

		/**
		 * Get the alpha value of the specified pixel
		 * 
		 * @param x The x coordinate of the images' pixel
		 * @param y The y coordinate of the images' pixel
		 * @return the alpha value in range [0-255] or -1 if alpha is not supported
		 * @since 1.3.0
		 */
		public int getAlpha(int x, int y) {
			if (!alpha)
				return -1;
			return imageData[getOffset(x, y)] & 0xFF;
		}

		/**
		 * Get the alpha component of the entire image mapped to a 2d array representing
		 * the x and y coordinates of the pixel.
		 * 
		 * @return the alpha values or null if alpha is not supported
		 * @since 1.3.0
		 */
		public int[][] getAlpha() {
			if (!alpha)
				return null;
			int[][] alpha = new int[width][height];
			int x = 0;
			int y = 0;
			for (int i = 0; i < imageData.length; i += bytesPerColor) {
				alpha[x][y] = (imageData[i] & 0xFF);
				x++;
				if (x >= width) {
					x = 0;
					y++;
				}
			}
			return alpha;
		}

		/**
		 * Set the alpha value of the specified pixel. This method is a NOP if alpha is
		 * not supported.
		 * 
		 * @param x        The x coordinate of the images' pixel
		 * @param y        The y coordinate of the images' pixel
		 * @param newAlpha the new alpha value in range [0-255]
		 * @since 1.3.0
		 */
		public void setAlpha(int x, int y, int newAlpha) {
			if (!alpha)
				return;
			imageData[getOffset(x, y)] = (byte) (newAlpha);
		}

		/**
		 * Get the red value of the specified pixel
		 * 
		 * @param x The x coordinate of the images' pixel
		 * @param y The y coordinate of the images' pixel
		 * @return the red value in range [0-255]
		 * @since 1.3.0
		 */
		public int getRed(int x, int y) {
			return imageData[getOffset(x, y) + alphaOffset + 2] & 0xFF;
		}

		/**
		 * Set the red value of the specified pixel
		 * 
		 * @param x      The x coordinate of the images' pixel
		 * @param y      The y coordinate of the images' pixel
		 * @param newRed the new red value in range [0-255]
		 * @since 1.3.0
		 */
		public void setRed(int x, int y, int newRed) {
			imageData[getOffset(x, y) + alphaOffset + 2] = (byte) (newRed);
		}

		/**
		 * Get the red component of the entire image mapped to a 2d array representing
		 * the x and y coordinates of the pixel.
		 * 
		 * @return the red values
		 * @since 1.3.0
		 */
		public int[][] getRed() {
			int[][] red = new int[width][height];
			int x = 0;
			int y = 0;
			for (int i = 2 + alphaOffset; i < imageData.length; i += bytesPerColor) {
				red[x][y] = (imageData[i] & 0xFF);
				x++;
				if (x >= width) {
					x = 0;
					y++;
				}
			}
			return red;
		}

		/**
		 * Get the green value of the specified pixel
		 * 
		 * @param x The x coordinate of the images' pixel
		 * @param y The y coordinate of the images' pixel
		 * @return the green value in range [0-255]
		 * @since 1.3.0
		 */
		public int getGreen(int x, int y) {
			return imageData[getOffset(x, y) + alphaOffset + 1] & 0xFF;
		}

		/**
		 * Set the green value of the specified pixel
		 * 
		 * @param x        The x coordinate of the images' pixel
		 * @param y        The y coordinate of the images' pixel
		 * @param newGreen the new green value in range [0-255]
		 * @since 1.3.0
		 */
		public void setGreen(int x, int y, int newGreen) {
			imageData[getOffset(x, y) + alphaOffset + 2] = (byte) (newGreen);
		}

		/**
		 * Get the green component of the entire image mapped to a 2d array representing
		 * the x and y coordinates of the pixel.
		 * 
		 * @return the green values
		 * @since 1.3.0
		 */
		public int[][] getGreen() {
			int[][] green = new int[width][height];
			int x = 0;
			int y = 0;
			for (int i = 1 + alphaOffset; i < imageData.length; i += bytesPerColor) {
				green[x][y] = (imageData[i] & 0xFF);
				x++;
				if (x >= width) {
					x = 0;
					y++;
				}
			}
			return green;
		}

		/**
		 * Get the blue value of the specified pixel
		 * 
		 * @param x The x coordinate of the images' pixel
		 * @param y The y coordinate of the images' pixel
		 * @return the blue value in range [0-255]
		 * @since 1.3.0
		 */
		public int getBlue(int x, int y) {
			return imageData[getOffset(x, y) + alphaOffset] & 0xFF;
		}

		/**
		 * Set the blue value of the specified pixel
		 * 
		 * @param x       The x coordinate of the images' pixel
		 * @param y       The y coordinate of the images' pixel
		 * @param newBlue the new blue value in range [0-255]
		 * @since 1.3.0
		 */
		public void setBlue(int x, int y, int newBlue) {
			imageData[getOffset(x, y) + alphaOffset + 2] = (byte) (newBlue);
		}

		/**
		 * Get the blue component of the entire image mapped to a 2d array representing
		 * the x and y coordinates of the pixel.
		 * 
		 * @return the blue values
		 * @since 1.3.0
		 */
		public int[][] getBlue() {
			int[][] blue = new int[width][height];
			int x = 0;
			int y = 0;
			for (int i = 0 + alphaOffset; i < imageData.length; i += bytesPerColor) {
				blue[x][y] = (imageData[i] & 0xFF);
				x++;
				if (x >= width) {
					x = 0;
					y++;
				}
			}
			return blue;
		}

		// YCrCb
		/**
		 * Return the Y(Luma) component of the YCbCr color model for the specified
		 * pixel.
		 * 
		 * @param x the x coordinate of the image
		 * @param y the y coordinate of the image
		 * @return the luma component in range [0-255]
		 * @since 1.3.0
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

		/**
		 * Return the Y(Luma) component of the YCbCr color model fof the entire image
		 * mapped to a 2d array representing the x and y coordinates of the pixel.
		 * 
		 * @return the luma component in range [0-255]
		 * @since 1.3.1
		 */
		public int[][] getLuma() {
			int luma[][] = new int[width][height];
			int x = 0;
			int y = 0;
			for (int i = 0 + alphaOffset; i < imageData.length; i += bytesPerColor) {
				int lum = (int) Math.round((imageData[i] & 0xFF) * ColorUtil.LUMA_RED
						+ (imageData[i+1] & 0xFF) * ColorUtil.LUMA_GREEN
						+ (imageData[i+2] & 0xFF) * ColorUtil.LUMA_BLUE);
				luma[x][y] = lum > 255 ? 255 : lum;
				x++;
				if (x >= width) {
					x = 0;
					y++;
				}
			}
			return luma;
		}

		/**
		 * Return the Cr(red-difference) component of the YCbCr color model for the
		 * specified pixel.
		 * 
		 * @param x the x coordinate of the image
		 * @param y the y coordinate of the image
		 * @return the cr component in range [0-255]
		 * @since 1.3.0
		 */
		public int getCr(int x, int y) {
			int index = (y * bytesPerColor * width) + (x * bytesPerColor);
			if (alpha)
				index++;
			int cr = (int) Math.round(
					(imageData[index++] & 0xFF) * ColorUtil.CR_RED + (imageData[index++] & 0xFF) * ColorUtil.CR_GREEN
							+ (imageData[index++] & 0xFF) * ColorUtil.CR_BLUE);
			return cr > 255 ? 255 : cr;
		}

		/**
		 * Return the Cb(blue-difference) component of the YCbCr color model for the
		 * specified pixel.
		 * 
		 * @param x the x coordinate of the image
		 * @param y the y coordinate of the image
		 * @return the cb component in range [0-255]
		 * @since 1.3.0
		 */
		public int getCb(int x, int y) {
			int index = (y * bytesPerColor * width) + (x * bytesPerColor);
			if (alpha)
				index++;
			int cb = (int) Math.round(
					(imageData[index++] & 0xFF) * ColorUtil.CB_RED + (imageData[index++] & 0xFF) * ColorUtil.CB_GREEN
							+ (imageData[index++] & 0xFF) * ColorUtil.CB_BLUE);
			return cb > 255 ? 255 : cb;
		}

		// HSV

		/**
		 * Return the hue component (angle) of the HSV color model for the specified pixel
		 * 
		 * @param x the x coordinate of the image
		 * @param y the y coordinate of the image
		 * @return the hue component in range [0-360]. As defined the hue is 0 for
		 *         undefined colors (e.g. white or black)
		 * @since 1.3.0
		 */
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

		/**
		 * Return the saturation component of the HSV color model for the specified
		 * pixel
		 * 
		 * <p>
		 * Note: Opposed to all other values for the hsb model saturation is returned as
		 * double in the range of [0-1] instead of [0-255] to allow for a higher
		 * accuracy.
		 * 
		 * @param x the x coordinate of the image
		 * @param y the y coordinate of the image
		 * @return the sat component in range [0-1]. As defined the sat is 0 for
		 *         undefined colors (i.e. black)
		 * @since 1.3.0
		 */
		public double getSat(int x, int y) {
			int offset = getOffset(x, y);
			int blue = imageData[offset + alphaOffset] & 0xFF;
			int green = imageData[offset + alphaOffset + 1] & 0xFF;
			int red = imageData[offset + alphaOffset + 2] & 0xFF;
			int max = Math.max(blue, Math.max(green, red));
			if (max == 0) {
				return 0;
			}
			int min = Math.min(blue, Math.min(green, red));
			return (max - min) / (double) max;
		}

		/**
		 * Return the value component of the HSV color model for the specified pixel
		 * 
		 * @param x the x coordinate of the image
		 * @param y the y coordinate of the image
		 * @return the value component in range [0-255].
		 */
		public int getVal(int x, int y) {
			int offset = getOffset(x, y);
			int blue = imageData[offset + alphaOffset] & 0xFF;
			int green = imageData[offset + alphaOffset + 1] & 0xFF;
			int red = imageData[offset + alphaOffset + 2] & 0xFF;
			int max = Math.max(blue, Math.max(green, red));
			return max;
		}

		private int getOffset(int x, int y) {
			return (y * bytesPerColor * width) + (x * bytesPerColor);
		}

		public boolean hasAlpha() {
			return alpha;
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
	 * @since 1.4.2 fixed not using awt rescale
	 */
	public static BufferedImage getScaledInstance(BufferedImage source, int width, int height) {
		BufferedImage target = new BufferedImage(width, height, source.getType());
		Graphics g = target.getGraphics();
		g.drawImage(source, 0, 0,width,height, null);
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

	private static BufferedImage bw;
	private static BufferedImage brown;

	public static void main(String[] args) {
		try {
			brown = ImageIO.read(ImageUtil.class.getClassLoader().getResourceAsStream("brown.png"));
			FastPixel fp = new ImageUtil.FastPixel(brown);
			System.out.println("Brown: " + fp.getSat(0, 0));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
